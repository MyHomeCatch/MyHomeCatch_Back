package org.scoula.ChatBot.Controller;

import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.service.LhNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private LhNoticeService lhNoticeService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> requestBody) {
        String userMessage = Optional.ofNullable(requestBody.get("message")).orElse("").toLowerCase();

        List<LhNoticeVO> notices;
        String reply;

        // 1️⃣ 인기/즐겨찾기 공고
        if (userMessage.contains("인기") || userMessage.contains("즐겨찾기")) {
            notices = lhNoticeService.getPopularLhNotices();
            reply = "가장 인기 있는 공고 5개를 알려드릴게요.\n" +
                    notices.stream()
                            .limit(5)
                            .map(this::formatNotice)
                            .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(Map.of("reply", reply));

            // 2️⃣ 추천 공고
        } else if (userMessage.contains("추천")) {
            notices = lhNoticeService.getAllLhNoticesNew();
            reply = "최신 공고 5개를 추천해 드릴게요.\n" +
                    notices.stream()
                            .sorted((n1, n2) -> n2.getPanNtStDt().compareTo(n1.getPanNtStDt()))
                            .limit(5)
                            .map(this::formatNotice)
                            .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(Map.of("reply", reply));

            // 3️⃣ 공고 목록
        } else if (userMessage.contains("공고")) {
            notices = lhNoticeService.getAllLhNoticesNew();
            reply = "최신 공고 10개를 알려드릴게요.\n" +
                    notices.stream()
                            .sorted((n1, n2) -> n2.getPanNtStDt().compareTo(n1.getPanNtStDt()))
                            .limit(10)
                            .map(this::formatNotice)
                            .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(Map.of("reply", reply));
        }

        // 4️⃣ 기본 Gemini API 호출
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", userMessage)));
        body.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", geminiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(geminiApiUrl, entity, Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> responseContent = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) responseContent.get("parts");
            String geminiReply = parts.get(0).get("text");

            return ResponseEntity.ok(Map.of("reply", geminiReply.trim()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("reply", "Gemini API 호출 중 오류 발생: " + e.getMessage()));
        }
    }

    private String formatNotice(LhNoticeVO notice) {
        return notice.getPanNm()
                + " (상태: " + notice.getPanSs()
                + ", 시작일: " + notice.getPanNtStDt() + ")"
                + "\n";
    }
}
