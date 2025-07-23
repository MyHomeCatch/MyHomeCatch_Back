package org.scoula.lh.service.lhHousing;

import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;

import java.util.List;

public interface LhHousingAttService {
    int create(LhHousingAttVO housingAttVO);
    int create(LhHousingAttDTO housingAttDTO);
    List<LhHousingAttDTO> getByPanId(String panId);
    LhHousingAttDTO getById(Integer lhHousingAttId);
    List<LhHousingAttDTO> getByBzdtNm(String bzdtNm);
}
