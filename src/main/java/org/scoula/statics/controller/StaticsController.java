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
@RequestMapping("/winner-stats")
public class StaticsController {

    private final StaticsService staticsService;

    @GetMapping("/region-score")
    public List<ScoreWinnerDTO> getScoreWinnersByRegion(@RequestParam("region") String regionCode) {
        log.info("청약 가점 통계 조회 요청 - regionCode: {}", regionCode);
        return staticsService.getScoreWinnersByRegion(regionCode);
    }

    @GetMapping("/low-score")
    public List<ApartmentScoreDTO> getLowScoreApartments(@RequestParam("region") String regionCode) {
        log.info("가점 하위 TOP5 조회 요청 - regionCode: {}", regionCode);
        return staticsService.getTop5ApartmentsWithLowestScore(regionCode);
    }
}
