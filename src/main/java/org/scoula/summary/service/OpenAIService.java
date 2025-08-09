package org.scoula.summary.service;

import org.scoula.summary.dto.OpenAIRequest;
import org.scoula.summary.dto.OpenAIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate;
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String chatWithOpenAI(String userMessage) {
        OpenAIRequest.Message message = new OpenAIRequest.Message("user", userMessage);
        OpenAIRequest request = new OpenAIRequest(model, Collections.singletonList(message), 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIRequest> requestEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, requestEntity, OpenAIResponse.class
            );

            OpenAIResponse responseBody = response.getBody();

            if (responseBody != null && responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
                return responseBody.getChoices().get(0).getMessage().getContent();
            } else {
                return "⚠️ OpenAI 응답이 비어 있거나 예상과 다릅니다.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ OpenAI 호출 중 오류 발생: " + e.getMessage();
        }
    }
}

