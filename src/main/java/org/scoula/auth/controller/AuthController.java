package org.scoula.auth.controller;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody LoginRequest request) {
//        return authServiceImpl.login(request);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(message);
        }

        return ResponseEntity.ok(authService.login(request));
    }

//    @PostMapping("/signup")
//    public AuthResponse signup(@RequestBody SignupRequest request) {
//        return authServiceImpl.signup(request);
//    }

    @PostMapping(value = "/signup", produces = "text/pain;charset=UTF-8")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(message);
        }

        authService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }
}
