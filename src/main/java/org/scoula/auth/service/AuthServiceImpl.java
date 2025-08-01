package org.scoula.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.KakaoLoginInfoDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.common.util.JwtUtil;
import org.scoula.member.dto.UserInfoDto;
import org.scoula.user.domain.User;
import org.scoula.auth.dto.GoogleUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean emailExists(String email) {
        return authMapper.findByEmail(email) != null;
    }

    @Override
    public boolean nicknameExists(String nickname) {
        return authMapper.findByNickname(nickname) != null;
    }

    @Value("${kakao.clientId}")
    private String clientId; // 카카오 로그인 클라이언트 ID

    @Value("${kakao.redirectUrl}")
    private String redirectUri; // 카카오 로그인 redirectURL


    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    private final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

    public AuthResponse login(LoginRequest request) {
        User user = authMapper.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        authMapper.saveRefreshToken(user.getUserId(), refreshToken);

        log.info("✅ 로그인 성공 - 이메일: {}, 닉네임: {}", user.getEmail(), user.getNickname());

        return new AuthResponse(accessToken, refreshToken, user.getNickname());
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (authMapper.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPw = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPw)
                .name(request.getName())
                .nickname(request.getNickname())
                .address(request.getAddress())
                .additionalPoint(0)
                .build();

        authMapper.insertUser(user);

        String token = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        authMapper.saveRefreshToken(user.getUserId(), refreshToken);

        return new AuthResponse(token, refreshToken, user.getNickname());
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        User user = authMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("삭제할 사용자를 찾을 수 없습니다: " + email);
        }
        authMapper.deleteByEmail(email);
    }

    @Override
    @Transactional
    public void deleteUserWithPasswordVerification(UserInfoDto userInfoDto) {
        String email = userInfoDto.getEmail();
        String currentPassword = userInfoDto.getCurrentPassword();

        User user = authMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("삭제할 사용자를 찾을 수 없습니다: " + email);
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        authMapper.deleteByEmail(email);
    }


    @Override
    public boolean resetPassword(String email, String newPassword) {
        // 1. 이메일 존재 확인
        if (!emailExists(email)) {
            return false;
        }

        // 2. 비밀번호 암호화 후 업데이트
        String hashedPassword = passwordEncoder.encode(newPassword);
        authMapper.updatePasswordByEmail(email, hashedPassword);
        return true;
    }

    public KakaoLoginInfoDto kakaoLogin(String code, HttpServletResponse httpServletResponse) {
        String url = "https://kauth.kakao.com/oauth/token";

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 바디 설정
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", clientId); // 본인 앱 키
        log.info(clientId);
        log.info(redirectUri);

        tokenParams.add("redirect_uri", redirectUri); // 프론트에서 사용한 redirect_uri
        tokenParams.add("code", code);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, headers);

        ResponseEntity<LinkedHashMap> tokenResponse = restTemplate.postForEntity(url, tokenRequest, LinkedHashMap.class);
        String accessToken = "Bearer "+ tokenResponse.getBody().get("access_token");;

        ResponseEntity<LinkedHashMap> response = fetchKakaoUserData(accessToken);

        Long id = (Long) response.getBody().get("id");

        LinkedHashMap<String, Object> userInfo = (LinkedHashMap<String, Object>) response.getBody().get("kakao_account");
        String kakaoEmail = userInfo.get("email").toString();
        User user = authMapper.findByEmail(kakaoEmail);

        if (user != null) {
            String token = jwtUtil.generateToken(kakaoEmail);
            String refreshToken = jwtUtil.generateRefreshToken(kakaoEmail);
            String nickname = user.getNickname();

            authMapper.saveRefreshToken(user.getUserId(), refreshToken);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 14); // 14일
            httpServletResponse.addCookie(refreshCookie);


            KakaoLoginInfoDto kakaoLoginInfoDto = KakaoLoginInfoDto.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .nickname(nickname)
                    .build();

            return kakaoLoginInfoDto;
        }
        else {
            LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) userInfo.get("profile");
            String profileUrl = (String) profile.get("profile_image_url");


            KakaoLoginInfoDto kakaoLoginInfoDto = KakaoLoginInfoDto.builder()
                    .id(id.toString())
                    .token(null)
//                    .nickname(userInfo.get("name").toString())
                    .profile(profileUrl)
                    .email(kakaoEmail)
                    .birthday(userInfo.get("birthday").toString())
                    .build();

            return kakaoLoginInfoDto;
        }
    }


    private ResponseEntity<LinkedHashMap> fetchKakaoUserData(String kakaoAccessToken) {

        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", kakaoAccessToken);

        HttpEntity<?> http = new HttpEntity<>(headers);

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(url, HttpMethod.GET, http, LinkedHashMap.class);

        return response;
    }

    @Override
    public ResponseEntity<?> googleSignupOrLogin(String code, HttpServletResponse httpServletResponse) {

        MultiValueMap<String, String> tokenRequestParams = new LinkedMultiValueMap<>();
        tokenRequestParams.add("code", code);
        tokenRequestParams.add("client_id", googleClientId);
        tokenRequestParams.add("client_secret", googleClientSecret);
        tokenRequestParams.add("redirect_uri", googleRedirectUri);
        tokenRequestParams.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenRequestParams, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(TOKEN_URI, tokenRequest, Map.class);


        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");


        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(USERINFO_URI, HttpMethod.GET, userInfoRequest, Map.class);


        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String googleEmail = (String) userInfo.get("email");

        GoogleUserDto googleUserDto = GoogleUserDto.builder()
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                .nickname((String) userInfo.get("nickname"))
                //.picture((String) userInfo.get("picture"))
                .build();

        User user = authMapper.findByEmail(googleUserDto.getEmail());

        if(user != null) {
            String token = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            authMapper.saveRefreshToken(user.getUserId(), refreshToken);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 14);
            httpServletResponse.addCookie(refreshCookie);

            googleUserDto.setToken(token);
            googleUserDto.setRefreshToken(refreshToken);
            googleUserDto.setNickname(user.getNickname());
        }
        googleUserDto.setId("1234");

        log.info("✅ 구글 로그인 - 이메일: {}, 닉네임: {}", googleEmail, user.getNickname());

        return ResponseEntity.ok(googleUserDto);

    }

}