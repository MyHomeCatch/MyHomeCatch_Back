package org.scoula.statics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.service.ApiDataLoaderService;
import org.scoula.statics.service.StaticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/winner-stats")
public class StaticsController {

    private final StaticsService service;
    private final ApiDataLoaderService loaderService;

    @GetMapping("/region-age")
    public ResponseEntity<?> getRegionAge(@RequestParam String region, @RequestParam String date) {
        return ResponseEntity.ok(service.getRegionAge(region, date));
    }

//    @GetMapping("/db")
//    public String saveApiData() {
//        loaderService.applicantApiSave("100", 2021);
//        return "api 호출";
//    }
}
