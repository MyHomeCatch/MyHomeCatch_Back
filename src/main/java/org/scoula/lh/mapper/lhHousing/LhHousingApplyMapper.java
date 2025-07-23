package org.scoula.lh.mapper.lhHousing;

import org.scoula.lh.domain.housing.LhHousingApplyVO;

import java.util.List;

public interface LhHousingApplyMapper {
    List<LhHousingApplyVO> getByPanId(String panId);
    int create(LhHousingApplyVO housingApply);
    int createAll(List<LhHousingApplyVO> housingApplies);
}
