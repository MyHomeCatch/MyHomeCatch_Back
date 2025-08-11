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
     * - 내부: PDF → 정제 텍스트 → 프롬프트 구성 → Gemini 호출 → MD 형식 응답 추출
     * - 출력: application/json (스키마 고정 아님: 문서에 있는 것만 포함)
     */
    @ApiOperation(value = "AI PDF 공고요약 ", notes = "PDF 공고를 AI를 통해 요약합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping(value = "/dynamic") // produces는 직접 헤더로 세팅
    public ResponseEntity<?> getDynamicSummary(
            @RequestParam("panId") String panId,
            @RequestParam("pdfUrl") String pdfUrl
    ) {
        try {
            String md = summaryService.getOrCreateMarkdownSummary(panId, pdfUrl);
            if (md == null || md.isBlank()) {
                // 생성 실패 or 내용 없음 → 저장 안 됨
                return ResponseEntity
                        .status(502)
                        .header("Content-Type", "text/plain; charset=UTF-8")
                        .body("요약 생성 실패(빈 결과)");
            }
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "text/markdown; charset=UTF-8")
                    .body(md);
        } catch (Exception e) {
            log.error("dynamic summary failed panId={}, pdfUrl={}, err={}", panId, pdfUrl, e.toString(), e);
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("요약 생성 실패: " + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    // ===== 공고요약 마크다운 특화 프롬프트 =====
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
