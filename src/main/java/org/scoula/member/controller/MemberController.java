package org.scoula.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.scoula.auth.dto.AuthResponse;
import org.scoula.auth.dto.PasswordResetRequestDto;
import org.scoula.auth.service.AuthService;
import org.scoula.common.response.CommonResponse;
import org.scoula.common.util.JwtUtil;
import org.scoula.member.dto.AdditionalPointDto;
import org.scoula.member.dto.UserInfoDto;
import org.scoula.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api(tags = "유저정보")
public class MemberController {

    private final MemberService memberService;

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        UserInfoDto userInfo = memberService.findUserInfoByEmail(email);

        if (userInfo == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(userInfo);
    }


    @PutMapping("/password-change")
    public ResponseEntity<?> changePassword(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) {
        String token = extractToken(request);
        // 토큰이 없거나 유효하지 않으면 401 Unauthorized 응답
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);
        userInfoDto.setEmail(email);

        try {
            memberService.updateUserInfo(userInfoDto);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    @PutMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        userInfoDto.setEmail(email);

        try {
            memberService.updateUserInfo(userInfoDto);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("현재 비밀번호가 일치하지 않습니다.")) {
                return ResponseEntity.status(401).body(e.getMessage());
            } else if (e.getMessage().contains("회원 정보를 수정하려면 현재 비밀번호를 입력해야 합니다.")) {
                return ResponseEntity.status(400).body(e.getMessage());
            } else if (e.getMessage().contains("사용자를 찾을 수 없습니다.")) {
                return ResponseEntity.status(404).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    @PostMapping("/password/reset")
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

    @ApiOperation(value = "가점 정보 업데이트", notes = "유저의 가점 정보를 업데이트 합니다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = AuthResponse.class),
            @ApiResponse(code = 401, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PutMapping("/additionalPoint")
    public ResponseEntity<?> updateAdditionalPoint(@RequestBody AdditionalPointDto additionalPointDto, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        memberService.updateAdditionalPoint(additionalPointDto);
        return ResponseEntity.ok(CommonResponse.response("가점 정보 업데이트"));

    }

}
