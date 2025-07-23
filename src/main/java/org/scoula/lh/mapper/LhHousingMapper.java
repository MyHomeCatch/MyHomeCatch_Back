package org.scoula.lh.mapper;

import org.scoula.lh.domain.LhHousingVO;

import java.util.List;

public interface LhHousingMapper {
    List<LhHousingVO> getByPanId(String panId);
    int create(LhHousingVO vo);
    int createAll(List<LhHousingVO> vo);
}
