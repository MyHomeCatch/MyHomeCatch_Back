package org.scoula.lh.mapper;

import org.scoula.lh.domain.LhHousingApplyVO;

import java.util.List;

public interface LhHousingApplyMapper {
    List<LhHousingApplyVO> getByPanId(String panId);
    int create(LhHousingApplyVO housingApply);
    int createAll(List<LhHousingApplyVO> housingApplies);
}
