package org.scoula.summary.service;

import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.NoticeAttVO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.scoula.lh.mapper.NoticeAttMapper;
import org.scoula.summary.mapper.SummaryMapper;
import org.scoula.summary.util.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private SummaryMapper summaryMapper;

    @Autowired
    private NoticeAttMapper noticeAttMapper;

    @Autowired
    private LhNoticeMapper lhNoticeMapper;

    @Override
    public String getOrCreateSummary(int danziId, String pdfUrl) {

        String summary = summaryMapper.findByPanId(danziId);
        if (summary != null) {
            return summary;
        }

        String text = pdfService.extractTextFromUrl(pdfUrl);

//        // 현재는 추출 내용 10글자 저장 -> GPT 연결하면 요약 내용을 저장
//        String shortSummary = text.replaceAll("\\s+", "")
//                .substring(0, Math.min(10, text.length()));

//        summaryMapper.insertSummary(panId, text);



        return text;
    }

    // 이미 요약이 있으면 그대로 반환, 없으면 생성해서 성공 시 저장
    @Transactional
    public String getOrCreateMarkdownSummary(int danziId, String pdfUrl) {

        String existing = summaryMapper.findByPanId(danziId);
        if (existing != null && !existing.isBlank()) {
            return existing;
        }

        // 1) PDF → 텍스트
        String baseText = pdfService.extractTextFromUrl(pdfUrl);
        if (baseText == null || baseText.isBlank()) {
            // 저장 없이 바로 리턴 (컨트롤러에서 그대로 내려주게끔)
            return "";
        }

        // 2) 프롬프트 구성
        String prompt = String.format(PROMPT_NOTICE_SUMMARY_MD, baseText);

        // 3) LLM 호출
        String md = null;
        try {
            md = geminiClient.generate(prompt);
            if (md != null) md = md.trim();
        } catch (Exception e) {
            // 실패하면 저장하지 않고 빈값/부분값 반환
            return "";
        }

        // 4) 생성물 검증: 비어있으면 저장하지 않음
        if (md == null || md.isBlank()) {
            return "";
        }

        // 5) 저장 (LONGTEXT/MEDIUMTEXT 컬럼 권장)
        summaryMapper.insertSummary(danziId, md);
        return md;
    }

    @Override
    public void fillAllSummaries() {
        int last = noticeAttMapper.getLast();
        for (int i = 1; i < last; i++) {
            Integer noticeId = i;
            List<NoticeAttVO> noticeAtts = noticeAttMapper.getNoticeAttByNoticeId(noticeId);
            if (noticeAtts != null && !noticeAtts.isEmpty()) {
                String pdfUrl = noticeAtts.get(0).getAhflUrl();
                if (pdfUrl != null && !pdfUrl.isBlank()) {
                    Integer danziId = lhNoticeMapper.getDanziId(noticeId);
                    if (danziId != null) {
                        try {
                            log.info("Summarizing noticeId={}, danziId={}, pdfUrl={}", noticeId, danziId, pdfUrl);
                            getOrCreateMarkdownSummary(danziId, pdfUrl);
                        } catch (Exception e) {
                            log.error("Failed to summarize noticeId={}, danziId={}, pdfUrl={}", noticeId, danziId, pdfUrl, e);
                        }
                    } else {
                        log.warn("danziId not found for noticeId={}", noticeId);
                    }
                } else {
                    log.warn("pdfUrl is blank for noticeId={}", noticeId);
                }
            } else {
                log.warn("noticeAtts not found for noticeId={}", noticeId);
            }
        }
    }

    public String getNoticeSummary(int danziId){
        return summaryMapper.findByPanId(danziId);
    }

    // 기존 문자열 상수 (서비스에 둬도 되고 컨트롤러에 둬도 됨)
    private static final String PROMPT_NOTICE_SUMMARY_MD = """
너는 LH 등 주택 모집 공고 PDF 요약 전문가다. 
아래 입력 텍스트(공고 원문 정제본)만을 근거로, 사람에게 보기 좋은 **마크다운 형식**으로 정리하라.
허구 금지, 모르면 생략하며, 반드시 문서에 있는 사실만 작성한다.

[출력 규칙]
- 제목은 "# 공고 요약"으로 시작
- 개요, 주요 내용, 추진 일정, 참고 링크 등을 섹션으로 구분
- 목록은 불릿 포인트("- ")로 작성
- 표는 마크다운 표 형식으로 작성
- 날짜는 YYYY-MM-DD 형식으로 표기
- 불필요한 서론·결론 없이 바로 요약 시작

[입력 텍스트]
%s
""";





}
