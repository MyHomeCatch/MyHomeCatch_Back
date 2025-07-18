package org.scoula.auth.controller;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}
