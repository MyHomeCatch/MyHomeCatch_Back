package org.scoula.lh.mapper;

import org.scoula.lh.domain.LhHousingAttVO;

import java.util.List;

public interface LhHousingAttMapper {
    List<LhHousingAttVO> getByPanId(String panId);
    List<LhHousingAttVO> getByBzdtNm(String bzdtNm);
    LhHousingAttVO getById(int id);
    int create(LhHousingAttVO housingAttVO);
    int createAll(List<LhHousingAttVO> housingAttVOs);
}
