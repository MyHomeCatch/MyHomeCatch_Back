package org.scoula.lh.service.lhHousing;

import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.dto.lhHousing.LhHousingDTO;

import java.util.List;

public interface LhHousingService {
    List<LhHousingDTO> getByPanId(String panId);
    int create(DanziVO dto);
    int createAll(List<LhHousingDTO> dtos);
}
