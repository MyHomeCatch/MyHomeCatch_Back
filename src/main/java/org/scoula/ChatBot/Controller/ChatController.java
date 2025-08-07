package org.scoula.ChatBot.Controller;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    // 예: https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> requestBody) {
        String userMessage = requestBody.get("message");

        // 요청 JSON 바디 생성 (공식 예시와 동일하게)
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", userMessage)));
        body.put("contents", List.of(content));

        // HTTP 헤더 설정: Content-Type + API 키
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", geminiApiKey);  // 여기서 API 키 헤더로 넣음

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // URL에 쿼리 파라미터(key)는 빼고 호출
            ResponseEntity<Map> response = restTemplate.postForEntity(geminiApiUrl, entity, Map.class);

            // 응답 파싱: candidates → content → parts → text
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> responseContent = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) responseContent.get("parts");
            String reply = parts.get(0).get("text");

            System.out.println("Response body: " + response.getBody());

            return ResponseEntity.ok(Map.of("reply", reply.trim()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("reply", "Gemini API 호출 중 오류 발생: " + e.getMessage()));
        }
    }
}
