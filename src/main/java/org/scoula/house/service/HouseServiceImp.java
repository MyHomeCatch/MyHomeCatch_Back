package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.domain.ApplyHomeAptPublicVO;
import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.mapper.ApplyHomeAPTMapper;
import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.dto.HousePage.*;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.scoula.house.mapper.HouseFilterMapper;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.scoula.house.util.RegionMapper;
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
    private final ApplyHomeAPTMapper applyHomeAPTMapper;
//    private final NoticeMapper noticeMapper;

    @Override
    public HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto) {
        // 필터 전처리와 지역 매핑을 한번에 처리
        requestDto.processFilters();

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

        if(table.equals("ahApt")) {
            return HouseDTO.ofApplyHomeAPTPublicVO(applyHomeAPTMapper.getByHouseNo(id));
        }

        return null;
    }

    // 정정공고 boolean 용(Att update용)
//    public boolean isCorrectedNotice(String panId) {
//        if (panId == null) return false;
//        String panSs = noticeMapper.getPanSsByPanId(panId);
//        return "정정공고".equals(panSs);
//    }
}
