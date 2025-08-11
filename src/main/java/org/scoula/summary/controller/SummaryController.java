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
    public void getSummary(@RequestParam("danziId") int danziId,
                           @RequestParam("pdfUrl") String pdfUrl,
                           HttpServletResponse response) {

        try {
            String summary = summaryService.getOrCreateSummary(danziId, pdfUrl);

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
     * 동적 구조의 공고요약 MD을 반환한다.
     * - 입력: danziId, pdfUrl
     * - 내부: PDF → 정제 텍스트 → 프롬프트 구성 → Gemini 호출 → MD 형식 응답 추출
     */
    @ApiOperation(value = "공고 PDF AI 요약 ", notes = "공고 PDF를 AI를 통해 요약하여 MD 형식 String 응답을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping(value = "/dynamic") // produces는 직접 헤더로 세팅
    public ResponseEntity<?> getDynamicSummary(
            @RequestParam("danziId") int danziId,
            @RequestParam("pdfUrl") String pdfUrl
    ) {
        try {
            String md = summaryService.getOrCreateMarkdownSummary(danziId, pdfUrl);
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
            log.error("dynamic summary failed danziId={}, pdfUrl={}, err={}", danziId, pdfUrl, e.toString(), e);
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("요약 생성 실패: " + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }







}
