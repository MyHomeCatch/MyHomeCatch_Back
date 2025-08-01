package org.scoula.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Response;
import lombok.extern.log4j.Log4j2;
import org.scoula.auth.dto.*;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.auth.service.AuthServiceImpl;
import org.scoula.common.util.JwtUtil;
import org.scoula.user.domain.User;
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

import javax.servlet.http.Cookie;
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
    private AuthServiceImpl authService;

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
    @Autowired
    private AuthMapper authMapper;


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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            String message = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

        AuthResponse authResponse = authService.login(request);

        // HttpOnly 쿠키에 저장
        Cookie refreshCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(Map.of(
                "token", authResponse.getAccessToken(),
                "nickname", authResponse.getNickname()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰");
        }

        String email = jwtUtil.extractEmail(token);
        User user = authMapper.findByEmail(email);

        authMapper.saveRefreshToken(user.getUserId(), null);

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 성공");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("쿠키가 없습니다.");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null || !jwtUtil.isValidToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프래시 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = authMapper.findByEmail(email);

        String storedRefreshToken = authMapper.getRefreshTokenByUserId(user.getUserId());
        if (!refreshToken.equals(storedRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 일치하지 않습니다.");
        }

        String newAccessToken = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token", newAccessToken));

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


    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴를 진행합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
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
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginDto kakaoLoginDto, HttpServletResponse httpServletResponse) {
        String code = kakaoLoginDto.getCode();

        KakaoLoginInfoDto kakaoLoginInfoDto = authService.kakaoLogin(code, httpServletResponse);

        return ResponseEntity.ok(kakaoLoginInfoDto);
    }

    @ApiOperation(value = "구글 로그인", notes = "구글 계정으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = GoogleUserDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload, HttpServletResponse httpServletResponse) {
        String code = payload.get("code");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "code 값이 누락되었습니다."));
        }

        return authService.googleSignupOrLogin(code, httpServletResponse);
    }

}