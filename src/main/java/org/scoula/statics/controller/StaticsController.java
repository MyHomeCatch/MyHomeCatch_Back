package org.scoula.statics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.service.ApiDataLoaderService;
import org.scoula.statics.service.StaticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

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
//    public String saveApiData() throws IOException, URISyntaxException {
//        for(int i=2020; i<=2025; i++) {
//            loaderService.applicantApiSave("100", i);
//            loaderService.winnerApiSave("100", i);
//        }
//        return "index";
//    }
}
