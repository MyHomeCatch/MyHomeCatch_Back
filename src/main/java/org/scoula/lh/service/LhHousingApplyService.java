package org.scoula.lh.service;

import org.scoula.lh.domain.LhHousingApplyVO;
import org.scoula.lh.dto.LhHousingApplyDTO;

import java.util.List;

public interface LhHousingApplyService {
    List<LhHousingApplyDTO> getAllByPanId(String panId);
    int create(LhHousingApplyDTO housingApplyDTO);
    int create(LhHousingApplyVO housingApplyVO);
    int createAll(List<LhHousingApplyDTO> housingApplyDTOs);
}
