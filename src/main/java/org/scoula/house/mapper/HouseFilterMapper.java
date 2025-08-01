package org.scoula.house.mapper;

import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;

import java.util.List;

public interface HouseFilterMapper {
    /**
     * 주택 목록 조회 (필터링 자동 적용)
     */
    List<HouseCardVO> getHousingList(HouseSearchRequestDTO searchDto);

    /**
     * 주택 목록 총 개수 조회 (필터링 자동 적용)
     */
    int getHousingCount(HouseSearchRequestDTO searchDto);

//    /**
//     * 모든 필터 옵션 조회 (한 번에)
//     */
//    List<FilterOptionVo> getFilterOptions();
}
