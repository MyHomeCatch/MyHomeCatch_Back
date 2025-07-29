package org.scoula.house.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.house.domain.ThumbVO;

import java.util.List;

public interface ThumbMapper {
    public int create(ThumbVO vo);
    List<ThumbVO> getImgPerPanId(String panId);
}
