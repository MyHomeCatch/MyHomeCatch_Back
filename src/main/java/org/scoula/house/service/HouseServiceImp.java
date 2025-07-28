package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HouseDTO;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class HouseServiceImp implements HouseService {

    private final LhRentalHouseMapper lhRentalHouseMapper;
    private final LhHousingHouseMapper lhHousingHouseMapper;

    @Override
    public List<HouseDTO> getHouses() {
        // 1. 데이터 조회 및 변환
        List<HouseDTO> rentalHouses = lhRentalHouseMapper.getRecentHouses().stream()
                .map(HouseDTO::ofLhRentalHouseVO)
                .toList();

        List<HouseDTO> housingHouses = lhHousingHouseMapper.getRecentHouses().stream()
                .map(HouseDTO::ofLhHousingHouseVO)
                .toList();

        // 2. 합치고 최신순 정렬
        return Stream.concat(rentalHouses.stream(), housingHouses.stream())
                .sorted(Comparator.comparing(HouseDTO::getNoticeDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    @Override
    public HouseDTO getHouse(String houseId) {
        String[] tokens = houseId.split("-");
        String table = tokens[0];
        String id = tokens[1];

        if(table.equals("lhrental")) {
            return HouseDTO.ofLhRentalHouseVO(lhRentalHouseMapper.get(Integer.parseInt(id)));
        }

        if(table.equals("lhhousing")) {
            return HouseDTO.ofLhHousingHouseVO(lhHousingHouseMapper.get(Integer.parseInt(id)));
        }

        return null;
    }
}
