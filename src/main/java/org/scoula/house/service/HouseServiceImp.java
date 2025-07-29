package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.dto.HousePage.*;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.scoula.house.mapper.HouseFilterMapper;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class HouseServiceImp implements HouseService {

    private final LhRentalHouseMapper lhRentalHouseMapper;
    private final LhHousingHouseMapper lhHousingHouseMapper;
    private final ApplyHomeOfficetelDetailMapper applyHomeOfficetelDetailMapper;
    private final HouseFilterMapper houseFilterMapper;

    @Override
    public HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto) {
        // DTO에 값이 있으면 자동으로 필터링됨
        List<HouseCardVO> voList = houseFilterMapper.getHousingList(requestDto);
        int totalCount = houseFilterMapper.getHousingCount(requestDto);

        List<HouseCardDTO> responseDtoList = voList.stream()
                .map(HouseCardDTO::ofHouseCardVO)
                .collect(Collectors.toList());

        PageInfoDTO pageInfo = PageInfoDTO.from(requestDto, totalCount);

        return HousePageResponseDTO.builder()
                .housingList(responseDtoList)
                .pageInfo(pageInfo)
                .build();
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

        if(table.equals("ahOfficetel")) {
            return HouseDTO.ofApplyHomeOfficetelHouseVO(applyHomeOfficetelDetailMapper.get(id));
        }

        return null;
    }
}
