package org.scoula.selfCheck.controller;

import org.scoula.auth.mapper.AuthMapper;
import org.scoula.common.util.JwtUtil;
import org.scoula.selfCheck.dto.SelfCheckRequestDto;
import org.scoula.selfCheck.mapper.SelfCheckMapper;
import org.scoula.selfCheck.service.SelfCheckService;
import org.scoula.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/self-check")
public class SelfCheckController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private SelfCheckMapper selfCheckMapper;

    @Autowired
    private SelfCheckService selfCheckService;

    private int extractUserIdFromToken(String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        User user = authMapper.findByEmail(email);
        return user != null ? user.getUserId() : -1;
    }

    @PostMapping("/diagnosis/Kookmin")
    public ResponseEntity<Map<String, Object>> diagnoseKookmin(@RequestHeader("Authorization") String tokenHeader,
                                                               @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateKookMin(dto, userId));
    }

    @PostMapping("/diagnosis/HengBok")
    public ResponseEntity<Map<String, Object>> diagnoseHengBok(@RequestHeader("Authorization") String tokenHeader,
                                                               @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateHengBok(dto, userId));
    }

    @PostMapping("/diagnosis/GongGong")
    public ResponseEntity<Map<String, Object>> diagnoseGongGong(@RequestHeader("Authorization") String tokenHeader,
                                                                @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateGongGong(dto, userId));
    }

    @PostMapping("/diagnosis/09")
    public ResponseEntity<Map<String, Object>> diagnose09(@RequestHeader("Authorization") String tokenHeader,
                                                          @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluate09(dto, userId));
    }

    @DeleteMapping("/init")
    public ResponseEntity<?> initialize(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");

        if (!jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);
        User user = authMapper.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        int userId = user.getUserId();
        selfCheckMapper.deleteByUserId(userId);

        return ResponseEntity.ok("기존 자격진단 결과 초기화 완료");
    }

    @GetMapping("/results")
    public ResponseEntity<?> getResults(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");

        if (!jwtUtil.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.extractEmail(token);
        User user = authMapper.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        int userId = user.getUserId();
        List<String> results = selfCheckMapper.findResultsByUserId(userId);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/content/save")
    public ResponseEntity<?> saveContent(@RequestHeader("Authorization") String tokenHeader,
                                         @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().body("유효하지 않은 사용자입니다.");

        selfCheckService.saveSelfCheckContent(dto, userId);
        return ResponseEntity.ok("진단 내용 저장 완료");
    }

    @DeleteMapping("/content/delete")
    public ResponseEntity<?> deleteContent(@RequestHeader("Authorization") String tokenHeader) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().body("유효하지 않은 사용자입니다.");

        selfCheckService.deleteSelfCheckContent(userId);
        return ResponseEntity.ok("진단 내용 삭제 완료");
    }
}