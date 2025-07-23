package org.scoula.statics.service;

import org.scoula.statics.dto.ApartmentScoreDTO;
import org.scoula.statics.dto.ScoreWinnerDTO;

import java.util.List;

public interface StaticsService {
    /**
     * 지역별 청약 가점제 정보 조회
     */
    List<ScoreWinnerDTO> getScoreWinnersByRegion(String region);

    /**
     * 지역별 가점 낮은 아파트 TOP 5 조회
     */
    List<ApartmentScoreDTO> getTop5ApartmentsWithLowestScore(String region);
}