package org.scoula.house.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.house.domain.LhNoticeVO;

import java.util.List;

public interface LhHouseNoticeMapper {
    List<LhNoticeVO> getMonthHouses(@Param("idList") List<String> idList);
}
