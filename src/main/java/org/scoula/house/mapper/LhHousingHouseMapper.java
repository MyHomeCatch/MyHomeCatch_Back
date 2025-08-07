package org.scoula.house.mapper;

import org.scoula.house.domain.LhHousingHouseVO;
import org.scoula.house.domain.LhRentalHouseVO;

import java.util.Date;
import java.util.List;

public interface LhHousingHouseMapper {
    List<LhHousingHouseVO> getRecentHouses();
    LhHousingHouseVO get(Integer lhHousinglId);
}
