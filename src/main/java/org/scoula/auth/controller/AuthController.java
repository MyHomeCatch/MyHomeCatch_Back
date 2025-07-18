package org.scoula.auth.controller;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.KakaoLoginDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authServiceImpl.login(request);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        return authServiceImpl.signup(request);
    }

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginDto kakaoLoginDto) {
        String code = kakaoLoginDto.getCode();

        AuthResponse authResponse = authServiceImpl.kakaoLogin(code);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/kakaoSignup")
    public ResponseEntity<?> kakaoSignup(@RequestBody KakaoLoginDto kakaoLoginDto) {

        return ResponseEntity.ok().build();
    }
}
