package org.scoula.lh.service.lhHousing;

import org.scoula.lh.dto.lhHousing.LhHousingDTO;

import java.util.List;

public interface LhHousingService {
    List<LhHousingDTO> getByPanId(String panId);
    int create(LhHousingDTO dto);
    int createAll(List<LhHousingDTO> dtos);
}
