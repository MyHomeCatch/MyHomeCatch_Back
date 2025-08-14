package org.scoula.summary.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.danzi.dto.http.DanziRequestDTO;
import org.scoula.lh.danzi.dto.http.DanziResponseDTO;
import org.scoula.lh.danzi.dto.http.PersonalizedCardDTO;
import org.scoula.lh.danzi.service.PersonalizedService;
import org.scoula.summary.service.ParsedSummaryService;
import org.scoula.summary.service.SummaryService;
import org.scoula.summary.util.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
@Log4j2
public class SummaryController {

    @Autowired
    private SummaryService summaryService;
    @Autowired
    private GeminiClient geminiClient;
    @Autowired
    private ParsedSummaryService parsedSummaryService;
    @Autowired
    private PersonalizedService personalizedService;

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

    @ApiOperation(value = "공고 PDF AI 요약 ", notes = "공고 PDF를 AI를 통해 요약하여 JSON 형식 String 응답을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDynamicJsonSummary(
            @RequestParam("danziId") int danziId,
            @RequestParam("pdfUrl") String pdfUrl
    ) {
        try {
            // 간단한 파라미터 검증
            if (pdfUrl == null || pdfUrl.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("pdfUrl 파라미터가 필요합니다.");
            }

            String json = summaryService.getOrCreateJsonSummary(danziId, pdfUrl);

            if (json == null || json.isBlank()) {
                // 502 Bad Gateway: 다운스트림(AI 요약) 실패 표현
                return ResponseEntity
                        .status(HttpStatus.BAD_GATEWAY)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("요약 생성 실패(빈 결과)");
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);

        } catch (Exception e) {
            log.error("dynamic summary failed danziId={}, pdfUrl={}, err={}", danziId, pdfUrl, e.toString(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("요약 생성 실패: " + (e.getMessage() == null ? "Unknown error" : e.getMessage()));
        }
    }

    @ApiOperation(value = "공고 요약 ", notes = "공고 요약의 요약을 Json 형식 응답으로 보냅니다.")
    @GetMapping(value = "/short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getShortSummary(@RequestParam("danziId") int danziId) {
        try {
            String json = summaryService.getNoticeSummary(danziId);
            if (json == null || json.isBlank()) {
                return ResponseEntity
                        .status(502)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("요약 생성 실패(빈 결과)");
            }

            JsonSummaryDTO dto = parsedSummaryService.createFromJson(danziId, json);
            return ResponseEntity.ok(Map.of("keyPoint", dto.getKeyPoints(),"target", dto.getTargetGroups(), "tier", dto.getSelectionCriteria() )); // 헤더 강제 설정 제거
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("요약 생성 실패: " + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }



    @ApiOperation(value = "자격진단 정보와 공고 요약문 비교", notes = "사용자 자가진단 내용이 공고문 요약에 해당하는지 비교합니다.")
    @PostMapping(value = "/personalCard/{houseId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPersonalCard(@RequestBody DanziRequestDTO requestDto, @PathVariable Integer houseId) {
        return ResponseEntity.ok(personalizedService.getOrCreatePersonalCard(houseId, requestDto.getUserId()));
    }

}
