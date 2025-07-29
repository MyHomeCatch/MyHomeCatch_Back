package org.scoula.member.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.common.util.JwtUtil;
import org.scoula.member.dto.UserInfoDto;
import org.scoula.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        // 사용자 정보 조회
        UserInfoDto userInfo = memberService.findUserInfoByEmail(email);
        if (userInfo == null) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(userInfo);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @PutMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);

        if (!email.equals(userInfoDto.getEmail())) {
            return ResponseEntity.status(403).body("권한이 없습니다: 토큰의 이메일과 요청 이메일이 일치하지 않습니다.");
        }

        try {
            memberService.updateUserInfo(userInfoDto);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            // 현재 비밀번호 불일치 또는 필수 필드 누락 등
            if (e.getMessage().contains("현재 비밀번호가 일치하지 않습니다.")) {
                return ResponseEntity.status(401).body(e.getMessage()); // 401 Unauthorized (인증 실패)
            } else if (e.getMessage().contains("회원 정보를 수정하려면 현재 비밀번호를 입력해야 합니다.")) {
                return ResponseEntity.status(400).body(e.getMessage()); // 400 Bad Request (잘못된 요청)
            } else if (e.getMessage().contains("사용자를 찾을 수 없습니다.")) {
                return ResponseEntity.status(404).body(e.getMessage()); // 404 Not Found
            }
            return ResponseEntity.status(400).body(e.getMessage()); // 기타 잘못된 요청
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
