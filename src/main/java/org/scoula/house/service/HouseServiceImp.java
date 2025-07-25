package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HouseDTO;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class HouseServiceImp implements HouseService {

    private final LhRentalHouseMapper lhRentalHouseMapper;

    @Override
    public HouseDTO getHouse(String houseId) {
        String[] tokens = houseId.split("-");
        String table = tokens[0];
        Integer pk = Integer.parseInt(tokens[1]);
        if(table.equals("lhrental")) {
            return HouseDTO.ofLhRentalHouseVO(lhRentalHouseMapper.get(pk));
        }
        return null;
    }
}
