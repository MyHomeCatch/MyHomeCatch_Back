package org.scoula.selfCheck.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.scoula.auth.mapper.AuthMapper;
import org.scoula.common.util.JwtUtil;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
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
@Api(tags = "Self Check API")
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
    @ApiOperation(value = "Diagnose Kookmin", notes = "Diagnose Kookmin self-check.")
    public ResponseEntity<Map<String, Object>> diagnoseKookmin(@RequestHeader("Authorization") String tokenHeader,
                                                               @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateKookMin(dto, userId));
    }

    @PostMapping("/diagnosis/HengBok")
    @ApiOperation(value = "Diagnose HengBok", notes = "Diagnose HengBok self-check.")
    public ResponseEntity<Map<String, Object>> diagnoseHengBok(@RequestHeader("Authorization") String tokenHeader,
                                                               @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateHengBok(dto, userId));
    }

    @PostMapping("/diagnosis/GongGong")
    @ApiOperation(value = "Diagnose GongGong", notes = "Diagnose GongGong self-check.")
    public ResponseEntity<Map<String, Object>> diagnoseGongGong(@RequestHeader("Authorization") String tokenHeader,
                                                                @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluateGongGong(dto, userId));
    }

    @PostMapping("/diagnosis/09")
    @ApiOperation(value = "Diagnose 09", notes = "Diagnose 09 self-check.")
    public ResponseEntity<Map<String, Object>> diagnose09(@RequestHeader("Authorization") String tokenHeader,
                                                          @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(selfCheckService.evaluate09(dto, userId));
    }

    @DeleteMapping("/init")
    @ApiOperation(value = "Initialize Self-Check", notes = "Initialize self-check results for the user.")
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
    @ApiOperation(value = "Get Self-Check Results", notes = "Get self-check results for the user.")
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
    @ApiOperation(value = "Save Self-Check Content", notes = "Save self-check content for the user.")
    public ResponseEntity<?> saveContent(@RequestHeader("Authorization") String tokenHeader,
                                         @RequestBody SelfCheckRequestDto dto) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().body("유효하지 않은 사용자입니다.");

        selfCheckService.saveSelfCheckContent(dto, userId);
        return ResponseEntity.ok("진단 내용 저장 완료");
    }

    @DeleteMapping("/content/delete")
    @ApiOperation(value = "Delete Self-Check Content", notes = "Delete self-check content for the user.")
    public ResponseEntity<?> deleteContent(@RequestHeader("Authorization") String tokenHeader) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().body("유효하지 않은 사용자입니다.");

        selfCheckService.deleteSelfCheckContent(userId);
        return ResponseEntity.ok("진단 내용 삭제 완료");
    }

    @GetMapping("/content")
    @ApiOperation(value = "Get Self-Check Content", notes = "Get self-check content for the user.")
    public ResponseEntity<?> getSelfCheckContent(@RequestHeader("Authorization") String tokenHeader) {
        int userId = extractUserIdFromToken(tokenHeader);
        if (userId == -1) return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");

        SelfCheckContentDto content = selfCheckService.getSelfCheckContent(userId);
        if (content == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("진단 내용이 없습니다.");
        }

        return ResponseEntity.ok(content);
    }
}