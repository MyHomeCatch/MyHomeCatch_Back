package org.scoula.statics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.dto.RegionAgeDTO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final StaticsMapper mapper;

    @Override
    public RegionAgeDTO getRegionAge(String region, String date) {
        long regionId = mapper.getRegionId(region);
        ApplicantRegionVO applicantVo = mapper.getApplicant(regionId, date);
        WinnerRegionVO winnerVo = mapper.getWinner(regionId, date);
        return RegionAgeDTO.of(applicantVo, winnerVo);
    }
}
