package org.scoula.summary.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * prompt를 넣으면 모델의 첫 번째 텍스트 응답을 그대로 돌려준다.
     */
    public String generate(String prompt) {
        // 여기서 값 찍어보기
        System.out.println("### geminiApiUrl = " + apiKey);
        System.out.println("### geminiApiKey = " + apiUrl);


        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 일부 환경은 쿼리파라미터 ?key=... 방식이 일반적이지만
        // 네 코드 스타일과 맞춰 헤더 사용. 필요하면 apiUrl + "?key=..."로 변경.
        headers.set("X-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.postForEntity(apiUrl, entity, Map.class);
        Map bodyMap = resp.getBody();
        if (bodyMap == null) throw new IllegalStateException("Empty Gemini response");

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) bodyMap.get("candidates");
        if (candidates == null || candidates.isEmpty()) return "";

        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        if (content == null) return "";

        List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
        if (parts == null || parts.isEmpty()) return "";

        String text = parts.get(0).get("text");
        return text == null ? "" : text;
    }
}
