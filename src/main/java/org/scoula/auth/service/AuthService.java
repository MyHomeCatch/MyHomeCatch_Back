package org.scoula.auth.service;

import org.scoula.auth.dto.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    boolean emailExists(String email);

    boolean nicknameExists(String nickname);

    AuthResponse login(LoginRequest request);

    AuthResponse signup(SignupRequest request);

    void deleteByEmail(String email);

    boolean resetPassword(String token, String newPassword);

    KakaoLoginInfoDto kakaoLogin(String code);

    ResponseEntity<?> googleSignupOrLogin(String code);
}
