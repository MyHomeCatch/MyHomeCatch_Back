package org.scoula.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleController {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

    @GetMapping("/login/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        response.sendRedirect(oauthUrl);
    }

    @GetMapping("/login/oauth2/code/google")
    public String googleCallback(@RequestParam("code") String code, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        // 1. AccessToken 요청을 위한 파라미터 세팅
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // 2. AccessToken 발급 요청
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(GOOGLE_TOKEN_URI, tokenRequest, Map.class);

        if (tokenResponse.getStatusCode() == HttpStatus.OK) {
            String accessToken = (String) tokenResponse.getBody().get("access_token");

            // 3. 사용자 정보 요청
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    GOOGLE_USERINFO_URI,
                    HttpMethod.GET,
                    userInfoRequest,
                    Map.class);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map userInfo = userInfoResponse.getBody();

                model.addAttribute("user", userInfo);
                return "googleProfile";
            }
        }

        model.addAttribute("error", "구글 로그인 실패");
        return "error_page";
    }
}
