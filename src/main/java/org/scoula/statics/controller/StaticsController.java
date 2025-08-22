package org.scoula.statics.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.statics.service.ApiDataLoaderService;
import org.scoula.statics.service.StaticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.scoula.statics.dto.ApartmentScoreDTO;
import org.scoula.statics.dto.ScoreWinnerDTO;
import org.scoula.statics.service.StaticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/winner-stats")
@Api(tags = "Statics API")
public class StaticsController {

    private final StaticsService service;
    private final ApiDataLoaderService loaderService;

    @GetMapping("/region-age")
    @ApiOperation(value = "Get Region Age", notes = "Get statistics by region and age.")
    public ResponseEntity<?> getRegionAge(@RequestParam String region, @RequestParam String date) {
        return ResponseEntity.ok(service.getRegionAge(region, date));
    }

    // 로그인 -> 유저 테이블에서 지역 가져오기
    @GetMapping("/low-popular")
    @ApiOperation(value = "Get Low Popular", notes = "Get apartments with low competition rates.")
    public ResponseEntity<?> getLowPopular(@RequestParam String region, @RequestParam String reside, @RequestParam int rank) {
        return ResponseEntity.ok(service.getLowCmpetRate(region, reside, rank));
    }

//    @GetMapping("/db")
//    public String saveApiData() throws IOException, URISyntaxException {
//        for(int i=2020; i<=2025; i++) {
//            loaderService.applicantApiSave("100", i);
//            loaderService.winnerApiSave("100", i);
//        }
//        return "index";
//    }

    @GetMapping("/region-score")
    @ApiOperation(value = "Get Score Winners by Region", notes = "Get a list of score winners by region.")
    public List<ScoreWinnerDTO> getScoreWinnersByRegion(@RequestParam("region") String regionCode) {
        log.info("청약 가점 통계 조회 요청 - regionCode: {}", regionCode);
        return service.getScoreWinnersByRegion(regionCode);
    }

    @GetMapping("/low-score")
    @ApiOperation(value = "Get Low Score Apartments", notes = "Get the top 5 apartments with the lowest scores.")
    public List<ApartmentScoreDTO> getLowScoreApartments(@RequestParam("region") String regionCode) {
        log.info("가점 하위 TOP5 조회 요청 - regionCode: {}", regionCode);
        return service.getTop5ApartmentsWithLowestScore(regionCode);
    }
}
