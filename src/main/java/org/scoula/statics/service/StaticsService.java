package org.scoula.statics.service;

import org.scoula.statics.dto.RegionAgeDTO;

public interface StaticsService {

    public RegionAgeDTO getRegionAge(String region, String date);
}
