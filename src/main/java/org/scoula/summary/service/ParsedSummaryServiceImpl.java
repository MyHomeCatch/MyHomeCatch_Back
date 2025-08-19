package org.scoula.summary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
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
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class ParsedSummaryServiceImpl implements ParsedSummaryService {

    private final SummaryMapper summaryMapper;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    @Transactional
    public NoticeSummaryDTO createFromMarkdown(int danziId, String markdown) {

        if (markdown == null) {
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

    @Override
    public JsonSummaryDTO getJsonSummary(int danziId) {
        return summaryMapper.findJsonDTOByDanziId(danziId);
    }

    @Override
    @Transactional
    public JsonSummaryDTO createFromJson(int danziId, String json) {
        if (json == null || json.trim().isEmpty()) {
            String fromDb = summaryMapper.findJsonByDanziId(danziId);
            if (fromDb == null || fromDb.trim().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "요약 원문(json)이 없습니다. danziId=" + danziId);
            }
            json = fromDb;
        }

        // 1) JSON 전처리 (```json ... ```, 잘못 들어온 백틱/주석 제거 등)
        String cleaned = sanitizeJson(json);

        // 2) 역직렬화 → DTO
        JsonSummaryDTO parsed;
        try {
            parsed = MAPPER.readValue(cleaned, JsonSummaryDTO.class);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "요약 JSON 파싱 실패: " + e.getMessage(), e);
        }

        // danziId 강제 세팅
        parsed.setDanziId(danziId);

        // 3) DTO 로드
        JsonSummaryDTO existing = summaryMapper.findJsonDTOByDanziId(danziId);
        JsonSummaryDTO toSave = mergeJson(existing, parsed);

        summaryMapper.insertSummaryJsonDTO(toSave);

        return toSave;
    }

    private JsonSummaryDTO mergeJson(JsonSummaryDTO oldDto, JsonSummaryDTO neu) {
        if (oldDto == null) return neu;

        JsonSummaryDTO out = new JsonSummaryDTO();
        out.setDanziId(oldDto.getDanziId());

        // 제목은 새 값 우선, 없으면 기존 유지
        out.setTitle(nvl(neu.getTitle(), oldDto.getTitle()));

        // 섹션별 새값 우선, 없으면 기존 유지
        out.setOverview(nvlList(neu.getOverview(), oldDto.getOverview()));
        out.setKeyPoints(nvlList(neu.getKeyPoints(), oldDto.getKeyPoints()));
        out.setTargetGroups(nvlList(neu.getTargetGroups(), oldDto.getTargetGroups()));

        out.setApplicationRequirements(
                nvlList(neu.getApplicationRequirements(), oldDto.getApplicationRequirements()));
        out.setRentalConditions(nvlList(neu.getRentalConditions(), oldDto.getRentalConditions()));
        out.setIncomeCriteria(nvlList(neu.getIncomeCriteria(), oldDto.getIncomeCriteria()));
        out.setAssetCriteria(nvlList(neu.getAssetCriteria(), oldDto.getAssetCriteria()));
        out.setSelectionCriteria(nvlList(neu.getSelectionCriteria(), oldDto.getSelectionCriteria()));
        out.setSchedule(nvlList(neu.getSchedule(), oldDto.getSchedule()));
        out.setRequiredDocuments(nvlList(neu.getRequiredDocuments(), oldDto.getRequiredDocuments()));
        out.setReferenceLinks(nvlList(neu.getReferenceLinks(), oldDto.getReferenceLinks()));

        return out;
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

    private static <T> List<T> nvlList(List<T> a, List<T> b) {
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


    private static String firstNonNull(String... s) {
        for (String v : s) if (v != null && !v.isEmpty()) return v;
        return null;
    }

    private String promoteBoldBulletsToH2(String md) {
        // "- **제목:**" 또는 "- **제목**" 형태를 "## 제목"으로 승격
        return md.replaceAll("(?m)^\\s*[-*]\\s*\\*\\*(.+?)\\*\\*\\s*:?.*$", "## $1");
    }



    /**
     * LLM/크롤링 결과에 종종 포함되는 ```json, ``` 같은 마크다운 펜스/주석을 제거하고
     * 불필요한 공백을 다듬는다.
     */
    private String sanitizeJson(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
// 1) UTF-8 BOM 제거
        if (s.startsWith("\uFEFF")) s = s.substring(1);

        // 2) 코드펜스 제거 ```json ... ```
        s = s.replaceAll("(?s)^\\s*```(?:json)?\\s*", "");
        s = s.replaceAll("(?s)```\\s*$", "");

        // 3) HTML 주석 제거 <!-- ... -->
        s = s.replaceAll("(?s)<!--.*?-->", "");

        // 4) JS 스타일 주석 제거 //... 및 /* ... */
        // 4-1) 문자열 내부의 //는 건드리지 않기 위해 아주 단순한 라인단위 제거(실제 문장에 '//'가 거의 없음 가정)
        s = s.replaceAll("(?m)^\\s*//.*$", "");
        // 4-2) 블록 주석
        s = s.replaceAll("(?s)/\\*.*?\\*/", "");

        // 5) 컨트롤 문자 제거(탭/개행 제외)
        s = s.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "");

        // 6) 트레일링 콤마 제거: ,]  ,}
        s = s.replaceAll(",\\s*([}\\]])", "$1");

        // 흔한 오탈자: 70%% → 70%
        s = s.replaceAll("(\\d+)%{2}", "$1%");
        // 트리밍
        return s.trim();
    }

}