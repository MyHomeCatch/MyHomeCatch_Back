package org.scoula.statics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.dto.RegionAgeDTO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final StaticsMapper mapper;

    @Override
    public RegionAgeDTO getRegionAge(String region) {
        int regionId = mapper.getRegionId(region);
        return RegionAgeDTO.of(null);
    }
}
