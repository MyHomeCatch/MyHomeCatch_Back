package org.scoula.summary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.scoula.lh.mapper.NoticeAttMapper;
import org.scoula.summary.mapper.SummaryMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.scoula.summary.parsing.MarkdownSectionExtractor.extractSection;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Log4j2
public class ParsedSummaryServiceImpl implements ParsedSummaryService {

    private final SummaryMapper summaryMapper;
    private final NoticeAttMapper noticeAttMapper;
    private final LhNoticeMapper lhNoticeMapper;

    @Override
    @Transactional
    public NoticeSummaryDTO createFromMarkdown(int danziId, String markdown) {

        if(markdown == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "요약 원문(markdown)이 없습니다. danziId=" + danziId);
        }


        NoticeSummaryDTO existing = summaryMapper.findDTOByDanziId(danziId);

        // 1) 마크다운 전처리 + 파싱
        String md = promoteBoldBulletsToH2(markdown);
        NoticeSummaryDTO parsed = parseToDto(danziId, md);

        // 2) 머지: parsed 값이 있으면 우선, 없으면 기존값 유지
        NoticeSummaryDTO toSave = merge(existing, parsed);

        // 3) 업서트
        summaryMapper.insertSummaryV2(toSave);

        return toSave;
    }

    private NoticeSummaryDTO merge(NoticeSummaryDTO oldDto, NoticeSummaryDTO neu) {
        if (oldDto == null) return neu;
        NoticeSummaryDTO out = new NoticeSummaryDTO();
        out.setDanziId(neu.getDanziId());
        out.setApplicationRequirements(nvl(neu.getApplicationRequirements(), oldDto.getApplicationRequirements()));
        out.setRentalConditions(nvl(neu.getRentalConditions(), oldDto.getRentalConditions()));
        out.setIncomeConditions(nvl(neu.getIncomeConditions(), oldDto.getIncomeConditions()));
        out.setAssetConditions(nvl(neu.getAssetConditions(), oldDto.getAssetConditions()));
        out.setSelectionCriteria(nvl(neu.getSelectionCriteria(), oldDto.getSelectionCriteria()));
        out.setSchedule(nvl(neu.getSchedule(), oldDto.getSchedule()));
        out.setRequiredDocuments(nvl(neu.getRequiredDocuments(), oldDto.getRequiredDocuments()));
        return out;
    }

    private static String nvl(String a, String b) {
        return (a != null && !a.isEmpty()) ? a : b;
    }

    private NoticeSummaryDTO parseToDto(int danziId, String markdown) {
        NoticeSummaryDTO dto = new NoticeSummaryDTO();
        dto.setDanziId(danziId);

        dto.setApplicationRequirements(firstNonNull(
                extractSection(markdown, "신청 자격", "신청자격", "신청 자격 상세 조건"),
                extractSection(markdown, "개요") // 그래도 없으면 개요로 대체
        ));

        dto.setRentalConditions(firstNonNull(
                extractSection(markdown, "임대 조건", "임대조건"),
                extractSection(markdown, "공급 대상") // 문서에 따라 여기에 묶여있는 경우
        ));

        String income = firstNonNull(
                extractSection(markdown, "소득 기준", "소득요건", "소득 및 자산보유 기준", "소득 및 자산 기준"),
                extractSection(markdown, "주요 내용")
        );
        String asset = firstNonNull(
                extractSection(markdown, "자산 기준", "자산요건", "소득 및 자산보유 기준", "소득 및 자산 기준"),
                income // 같은 섹션이면 복제
        );
        dto.setIncomeConditions(income);
        dto.setAssetConditions(asset);

        dto.setSelectionCriteria(firstNonNull(
                extractSection(markdown, "선정 기준"),
                extractSection(markdown, "배점 기준", "배점기준"),
                extractSection(markdown, "입주자 선정 방법")
        ));

        dto.setSchedule(firstNonNull(
                extractSection(markdown, "추진 일정"),
                extractSection(markdown, "모집 일정", "주요 일정")
        ));

        dto.setRequiredDocuments(extractSection(markdown, "제출 서류"));
        return dto;
    }


    private LocalDate parseDate(String raw) {
        if (raw == null) return null;
        try {
            return LocalDate.parse(raw);
        } catch (Exception e) {
            return null;
        }
    }

    private static String firstNonNull(String... s) {
        for (String v : s) if (v != null && !v.isEmpty()) return v;
        return null;
    }

    private String promoteBoldBulletsToH2(String md) {
        // "- **제목:**" 또는 "- **제목**" 형태를 "## 제목"으로 승격
        return md.replaceAll("(?m)^\\s*[-*]\\s*\\*\\*(.+?)\\*\\*\\s*:?.*$", "## $1");
    }

    @Override
    public NoticeSummaryDTO getSummary(int danziId) {
        return summaryMapper.findDTOByDanziId(danziId);
    }
}
