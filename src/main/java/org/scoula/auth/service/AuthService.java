package org.scoula.auth.service;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;

import java.util.LinkedHashMap;

public interface AuthService {
    AuthResponse login(LoginRequest request);

    AuthResponse signup(SignupRequest request);

    AuthResponse kakaoLogin(String code);
}
