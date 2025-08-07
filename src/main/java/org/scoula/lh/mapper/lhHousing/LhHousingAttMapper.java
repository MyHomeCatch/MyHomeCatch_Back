package org.scoula.lh.mapper.lhHousing;

import org.scoula.lh.domain.housing.LhHousingAttVO;

import java.util.List;

public interface LhHousingAttMapper {
    List<LhHousingAttVO> getByPanId(String panId);
    List<LhHousingAttVO> getByBzdtNm(String bzdtNm);
    List<LhHousingAttVO> getAll();
    LhHousingAttVO getById(int id);
    int create(LhHousingAttVO housingAttVO);
    int createAll(List<LhHousingAttVO> housingAttVOs);
}
