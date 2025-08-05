package org.scoula.auth.service;

import org.scoula.auth.dto.*;
import org.springframework.http.ResponseEntity;
import org.scoula.member.dto.UserInfoDto;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean emailExists(String email);

    boolean nicknameExists(String nickname);

    AuthResponse login(LoginRequest request);

    AuthResponse signup(SignupRequest request);

    void deleteByEmail(String email);

    void deleteUserWithPasswordVerification(UserInfoDto userInfoDto);

    boolean resetPassword(String token, String newPassword);

    KakaoLoginInfoDto kakaoLogin(String code, HttpServletResponse response);

    ResponseEntity<?> googleSignupOrLogin(String code, HttpServletResponse response);
}
