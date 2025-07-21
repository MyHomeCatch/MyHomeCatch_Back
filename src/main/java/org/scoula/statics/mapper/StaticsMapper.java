package org.scoula.statics.mapper;

import org.scoula.statics.dto.ApartmentScoreDTO;
import org.scoula.statics.dto.ScoreWinnerDTO;

import java.util.List;

public interface StaticsMapper {
    List<ScoreWinnerDTO> getScoreWinnersByRegion(String region);

    List<ApartmentScoreDTO> getTop5ApartmentsWithLowestScore(String region);
}
