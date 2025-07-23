package org.scoula.lh.service;

import org.scoula.lh.domain.LhHousingAttVO;
import org.scoula.lh.dto.LhHousingAttDTO;

import java.util.List;

public interface LhHousingAttService {
    int create(LhHousingAttVO housingAttVO);
    int create(LhHousingAttDTO housingAttDTO);
    List<LhHousingAttDTO> getByPanId(String panId);
    LhHousingAttDTO getById(Integer lhHousingAttId);
    List<LhHousingAttDTO> getByBzdtNm(String bzdtNm);
}
