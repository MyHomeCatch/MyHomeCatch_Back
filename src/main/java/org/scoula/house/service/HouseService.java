package org.scoula.house.service;

import org.scoula.house.dto.HouseDTO;

import java.util.Date;
import java.util.List;

public interface HouseService {
    List<HouseDTO> getHouses();
    HouseDTO getHouse(String houseId);
}
