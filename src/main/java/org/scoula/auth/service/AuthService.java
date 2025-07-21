package org.scoula.auth.service;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.GoogleUserDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);

    AuthResponse signup(SignupRequest request);

    AuthResponse googleSignupOrLogin(GoogleUserDto googleUserDto);
}
