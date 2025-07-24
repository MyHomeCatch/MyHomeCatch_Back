package org.scoula.lh.service.lhHousing;

import org.scoula.lh.domain.housing.LhHousingApplyVO;
import org.scoula.lh.dto.lhHousing.LhHousingApplyDTO;

import java.util.List;

public interface LhHousingApplyService {
    List<LhHousingApplyDTO> getAllByPanId(String panId);
    int create(LhHousingApplyDTO housingApplyDTO);
    int create(LhHousingApplyVO housingApplyVO);
    int createAll(List<LhHousingApplyDTO> housingApplyDTOs);
}
