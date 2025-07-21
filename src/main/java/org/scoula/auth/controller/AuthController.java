package org.scoula.auth.controller;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.auth.service.AuthServiceImpl;
import org.scoula.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean exists = authService.nicknameExists(nickname);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

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

            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

        return ResponseEntity.ok(authService.login(request));
    }

//    @PostMapping("/signup")
//    public AuthResponse signup(@RequestBody SignupRequest request) {
//        return authServiceImpl.signup(request);
//    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String message = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", message);
            return ResponseEntity.badRequest().body(errorBody);
        }

        authService.signup(request);

        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(Map.of("message", "유효하지 않은 토큰"));
        }

        String email = jwtUtil.extractEmail(token);

        authService.deleteByEmail(email);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 완료"));
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
