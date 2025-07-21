package org.scoula.statics.service;

import lombok.RequiredArgsConstructor;
import org.scoula.statics.dto.ApartmentScoreDTO;
import org.scoula.statics.dto.ScoreWinnerDTO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final StaticsMapper staticsMapper;

    @Override
    public List<ScoreWinnerDTO> getScoreWinnersByRegion(String region) {
        return staticsMapper.getScoreWinnersByRegion(region);
    }

    @Override
    public List<ApartmentScoreDTO> getTop5ApartmentsWithLowestScore(String region) {
        return staticsMapper.getTop5ApartmentsWithLowestScore(region);
    }
}