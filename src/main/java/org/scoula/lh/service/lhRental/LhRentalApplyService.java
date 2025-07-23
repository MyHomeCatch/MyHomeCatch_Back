package org.scoula.lh.service.lhRental;

import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.dto.lhRental.LhRentalApplyDTO;

import java.util.List;

public interface LhRentalApplyService {
    LhRentalApplyDTO getById(Integer id);
    List<LhRentalApplyDTO> getByPanId(String panId);
    int create(LhRentalApplyDTO lhRentalApplyDTO);
    int create(LhRentalApplyVO lhRentalApplyVO);
}
