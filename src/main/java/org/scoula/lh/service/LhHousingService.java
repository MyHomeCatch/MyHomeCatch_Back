package org.scoula.lh.service;

import org.scoula.lh.dto.LhHousingDTO;

import java.util.List;

public interface LhHousingService {
    List<LhHousingDTO> getByPanId(String panId);
    int create(LhHousingDTO dto);
    int createAll(List<LhHousingDTO> dtos);
}
