package org.scoula.lh.mapper.lhHousing;

import org.scoula.lh.domain.housing.LhHousingVO;

import java.util.List;

public interface LhHousingMapper {
    List<LhHousingVO> getByPanId(String panId);
    int create(LhHousingVO vo);
    int createAll(List<LhHousingVO> vo);
}
