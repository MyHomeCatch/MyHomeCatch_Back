package org.scoula.auth.service;

import org.scoula.auth.dto.*;

public interface AuthService {
    boolean emailExists(String email);

    boolean nicknameExists(String nickname);

    AuthResponse login(LoginRequest request);

    AuthResponse signup(SignupRequest request);

    void deleteByEmail(String email);

    KakaoLoginInfoDto kakaoLogin(String code);

    AuthResponse googleSignupOrLogin(GoogleUserDto googleUserDto);
}
