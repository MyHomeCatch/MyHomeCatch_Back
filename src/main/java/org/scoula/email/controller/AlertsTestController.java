package org.scoula.email.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.email.scheduled.AlertScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test/alert")
@RequiredArgsConstructor
public class AlertsTestController {

    private final AlertScheduler alertScheduler;

    // 테스트용 코드
    @GetMapping("/run")
    public ResponseEntity<Map<String, String>> runAlert() {
        alertScheduler.sendApplicationDateAlert();
        return ResponseEntity.ok(Map.of("message", "알림 테스트 실행됨"));
    }
}
