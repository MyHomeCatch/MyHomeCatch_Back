package org.scoula.statics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping("/api/winner-stats")
public class StaticsController {

    private final StaticsService staticsService;

//    지역별 청약 가점제 당첨자 정보 조회
    @GetMapping("/region-score")
    public List<ScoreWinnerDTO> getScoreWinnersByRegion(@RequestParam("region") String region) {
        log.info("청약 가점 당첨자 통계 조회 요청 - region: {}", region);
        return staticsService.getScoreWinnersByRegion(region);
    }

    @GetMapping("/low-popular")
    public List<ApartmentScoreDTO> getLowScoreApartments(@RequestParam("region") String region) {
        log.info("가점 하위 TOP5 조회 요청 - region: {}", region);
        return staticsService.getTop5ApartmentsWithLowestScore(region);
    }
}
