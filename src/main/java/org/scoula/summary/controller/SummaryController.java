package org.scoula.summary.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.summary.service.SummaryService;
import org.scoula.summary.util.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/summary")
@Log4j2
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private GeminiClient geminiClient;

    @GetMapping
    public void getSummary(@RequestParam("panId") String panId,
                           @RequestParam("pdfUrl") String pdfUrl,
                           HttpServletResponse response) {

        try {
            String summary = summaryService.getOrCreateSummary(panId, pdfUrl);

            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(summary);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("요약 실패: " + e.getMessage());
            } catch (IOException ioException) {
            }
        }
    }

    /**
     * 동적 구조의 공고요약 JSON을 반환한다.
     * - 입력: panId, pdfUrl
     * - 내부: PDF → 정제 텍스트 → 프롬프트 구성 → Gemini 호출 → JSON만 추출
     * - 출력: application/json (스키마 고정 아님: 문서에 있는 것만 포함)
     */
    @ApiOperation(value = "AI PDF 공고요약 ", notes = "PDF 공고를 AI를 통해 요약합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping(value = "/dynamic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDynamicSummary(
            @RequestParam("panId") String panId,
            @RequestParam("pdfUrl") String pdfUrl
    ) {
        try {
            // 1) PDF 원문 → 정제 텍스트 (또는 1차 요약) 확보
            String baseText = summaryService.getOrCreateSummary(panId, pdfUrl);
            log.info("extracted length={}", baseText == null ? 0 : baseText.length());

            // 2) 공고요약 특화 프롬프트(동적 구조, 허구 금지)
            String finalPrompt = String.format(PROMPT_NOTICE_SUMMARY, baseText);

            // 3) Gemini 호출
            String raw = geminiClient.generate(finalPrompt).trim();
            log.info("gemini raw first100={}", raw == null ? null : raw.substring(0, Math.min(100, raw.length())));

            // 4) 모델이 JSON만 출력하도록 강하게 지시했지만
            // 혹시 앞뒤에 잡말이 섞이면 JSON 부분만 추출
            String json = extractJsonOrWrap(raw);

            // 그대로 JSON 반환 (문자열 본문)
            return ResponseEntity.ok(json);

        } catch (Exception e) {
            log.error("dynamic summary failed panId={}, pdfUrl={}, err={}", panId, pdfUrl, e.toString(), e);
            return ResponseEntity.status(500).body("{\"message\":\"요약 생성 실패\",\"error\":\"" +
                    escape(e.getMessage()) + "\"}");
        }
    }

    /**
     * 모델 출력에서 JSON만 뽑아내거나, 실패 시 summary 한 줄로 감싸기
     */
    private String extractJsonOrWrap(String s) {
        int i = s.indexOf('{');
        int j = s.lastIndexOf('}');
        if (i >= 0 && j > i) {
            return s.substring(i, j + 1);
        }
        // JSON이 아닐 경우 최소 래핑
        return "{\"summary\":\"" + escape(s) + "\"}";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // ===== 공고요약 특화 프롬프트 =====
    private static final String PROMPT_NOTICE_SUMMARY = """
너는 LH 등 주택 모집 공고 PDF 요약 전문가다. 아래 입력 텍스트(공고 원문 정제본)만을 근거로 요약하라.
출력은 반드시 JSON 하나만 포함해야 하며, 키는 아래 '출력 규칙'을 따른다. 허구 금지, 모르면 생략.

[출력 규칙]
- 최상위는 다음 키를 가질 수 있다(존재하는 것만 포함):
  - "summary": 한 단락 요약(최대 3문장)
  - "sections": 섹션 배열. 각 섹션은 { "title": string, "bullets": string[] }
  - "fields": { "name": string, "value": string }[]  (예: "접수기간", "대상/자격", "공급규모", "지역", "신청방법", "제출서류", "유의사항", "문의처", "청약접수일정", "계약기간", "분양가격", "전용면적", "우선공급대상", "무순위청약", "재당첨제한")
  - "tables": { "title": string, "rows": string[][] }
  - "links": { "label": string, "url": string }
  - "citations": { "quote": string, "page": number }
- 문서에 없는 정보는 키 자체를 생략한다(빈 값 금지).
- 날짜는 가능하면 YYYY-MM-DD로 정규화한다.
- 출력은 반드시 JSON 객체 하나여야 하며, JSON 시작 전과 끝 이후에는 아무것도 출력하지 않는다.

[입력 텍스트]
%s
""";





}
