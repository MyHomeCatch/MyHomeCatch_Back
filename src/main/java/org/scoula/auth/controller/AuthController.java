package org.scoula.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.dto.*;
import org.scoula.auth.service.AuthService;
import org.scoula.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.GoogleUserDto;
import org.scoula.auth.dto.LoginRequest;
import org.scoula.auth.dto.SignupRequest;
import org.scoula.member.dto.UserInfoDto; // UserInfoDto 임포트 추가
import org.springframework.http.*;

import org.springframework.ui.Model;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@RestController
@RequestMapping("/api/auth")
@Log4j2
@Api(tags = "로그인")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";


    @ApiOperation(value = "이메일 중복 체크", notes = "이메일 중복 여부를 체크합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

    @ApiOperation(value = "닉네임 중복 체크", notes = "닉네임 중복 여부를 체크합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean exists = authService.nicknameExists(nickname);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

    @ApiOperation(value = "일반 로그인", notes = "일반 계정으로 로그인을 진행합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
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

    @ApiOperation(value = "일반 회원가입", notes = "입력한 정보로 회원가입을 진행합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
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


    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴를 진행합니다 (현재 비밀번호 검증 포함)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다 (비밀번호 불일치 등)."),
            @ApiResponse(code = 401, message = "유효하지 않은 토큰입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) {
        String token = extractToken(request);
        System.out.println("====== withdraw called ======");

        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(Map.of("message", "유효하지 않은 토큰입니다."));
        }

        String email = jwtUtil.extractEmail(token);

        userInfoDto.setEmail(email);

        try {
            authService.deleteUserWithPasswordVerification(userInfoDto);
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @ApiOperation(value = "비밀번호 변경", notes = "해당 이메일의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping("/password/reset")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDto dto) {
        System.out.println("비밀번호 변경 요청 이메일: " + dto.getEmail());
        System.out.println("비밀번호 변경 요청 newPassword: " + dto.getNewPassword());

        boolean result = authService.resetPassword(dto.getEmail(), dto.getNewPassword());
        if (result) {
            return ResponseEntity.ok(Map.of("success", true, "message", "비밀번호가 성공적으로 변경되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "해당 이메일을 가진 사용자를 찾을 수 없습니다."));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }


    @ApiOperation(value = "카카오 로그인", notes = "카카오 계정으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginDto kakaoLoginDto) {
        String code = kakaoLoginDto.getCode();

        KakaoLoginInfoDto kakaoLoginInfoDto = authService.kakaoLogin(code);

        return ResponseEntity.ok(kakaoLoginInfoDto);
    }

    @ApiOperation(value = "구글 로그인", notes = "구글 계정으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = GoogleUserDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "code 값이 누락되었습니다."));
        }

        return authService.googleSignupOrLogin(code);
    }
}