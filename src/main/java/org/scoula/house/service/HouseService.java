package org.scoula.house.service;

import org.scoula.house.dto.HousePage.HouseCardDTO;
import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.dto.HousePage.HousePageResponseDTO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;
import org.scoula.lh.danzi.dto.DanziRequestDTO;
import org.scoula.lh.danzi.dto.DanziResponseDTO;

public interface HouseService {
    HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto);
    DanziResponseDTO getHouse(Integer houseId);
    DanziResponseDTO getHouseWithUserData(DanziRequestDTO requestDto, Integer houseId);
    HouseCardDTO getHouseCard(Integer houseId);
}
