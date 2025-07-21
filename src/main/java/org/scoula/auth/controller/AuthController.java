package org.scoula.auth.controller;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.GoogleUserDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

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

        MultiValueMap<String, String> tokenRequestParams = new LinkedMultiValueMap<>();
        tokenRequestParams.add("code", code);
        tokenRequestParams.add("client_id", clientId);
        tokenRequestParams.add("client_secret", clientSecret);
        tokenRequestParams.add("redirect_uri", redirectUri);
        tokenRequestParams.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenRequestParams, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(TOKEN_URI, tokenRequest, Map.class);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("error", "구글 토큰 요청 실패");
            return "error_page";
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");


        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(USERINFO_URI, HttpMethod.GET, userInfoRequest, Map.class);
        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("error", "구글 사용자 정보 요청 실패");
            return "error_page";
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();


        GoogleUserDto googleUserDto = GoogleUserDto.builder()
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                //.picture((String) userInfo.get("picture"))
                .build();


        AuthResponse authResponse = authService.googleSignupOrLogin(googleUserDto);


        model.addAttribute("user", googleUserDto);

        model.addAttribute("token", authResponse.getToken());
        model.addAttribute("nickname", authResponse.getNickname());

        return "googleProfile";
    }
}
