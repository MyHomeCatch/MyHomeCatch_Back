package org.scoula.house.service;

import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.dto.HousePage.HousePageResponseDTO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;

public interface HouseService {
    HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto);
    HouseDTO getHouse(String houseId);
}
