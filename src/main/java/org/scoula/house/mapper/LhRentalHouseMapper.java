package org.scoula.house.mapper;

import org.scoula.house.domain.LhRentalHouseVO;

import java.util.Date;
import java.util.List;

public interface LhRentalHouseMapper {
    List<LhRentalHouseVO> getRecentHouses();
    LhRentalHouseVO get(Integer lhRentalId);
}
