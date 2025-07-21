package org.scoula.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.KakaoLoginInfoDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.common.util.JwtUtil;
import org.scoula.user.domain.User;
import org.scoula.auth.dto.GoogleUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

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

    public AuthResponse login(LoginRequest request) {
        User user = authMapper.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getNickname());
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
        return new AuthResponse(token, user.getNickname());
    }

    @Override
    public void deleteByEmail(String email) {
        authMapper.deleteByEmail(email);
    }


    public KakaoLoginInfoDto kakaoLogin(String code) {
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

        // 요청
        ResponseEntity<LinkedHashMap> tokenResponse = restTemplate.postForEntity(url, tokenRequest, LinkedHashMap.class);
        String accessToken = "Bearer "+ tokenResponse.getBody().get("access_token");;
        // 사용자 정보 가져오기 응답
        ResponseEntity<LinkedHashMap> response = fetchKakaoUserData(accessToken);

        // 카카오 로그인 password에 적용할 ID
        // Ex) Kakao + ID
        Long id = (Long) response.getBody().get("id");
        // 카카오 사용자 정보
        LinkedHashMap<String, Object> userInfo = (LinkedHashMap<String, Object>) response.getBody().get("kakao_account");
        String kakaoEmail = userInfo.get("email").toString();
        User user = authMapper.findByEmail(kakaoEmail);

        if (user != null) {
            String token = jwtUtil.generateToken(kakaoEmail);
            String nickname = user.getNickname();

            KakaoLoginInfoDto kakaoLoginInfoDto = KakaoLoginInfoDto.builder()
                    .token(token)
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


    // kakaoAccessToken을 사용하여 카카오 서버로부터 유저 정보 받아오기
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
    public AuthResponse googleSignupOrLogin(GoogleUserDto googleUserDto) {
        User user = authMapper.findByEmail(googleUserDto.getEmail());

        if (user == null) {
            user = new User();
            user.setEmail(googleUserDto.getEmail());
            user.setName(googleUserDto.getName());
            user.setNickname(googleUserDto.getName());
            user.setAdditionalPoint(0);
            // 구글 로그인이라 비밀번호 없을 수 있음, 비워두거나 랜덤값 넣거나 null 처리
            user.setPassword("");

            authMapper.insertUser(user);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getNickname());
    }

}