package org.scoula.statics.service;

import org.scoula.statics.dto.LowCompetitionRateDTO;
import org.scoula.statics.dto.RegionAgeDTO;

import java.util.List;

public interface StaticsService {

    public RegionAgeDTO getRegionAge(String region, String date);

    public List<LowCompetitionRateDTO> getLowCmpetRate(String region, String reside, int rank);
}
