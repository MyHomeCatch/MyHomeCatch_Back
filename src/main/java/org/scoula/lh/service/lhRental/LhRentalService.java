package org.scoula.lh.service.lhRental;

import org.scoula.lh.domain.rental.LhRentalVO;
import org.scoula.lh.dto.lhRental.LhRentalDTO;

import java.util.List;

public interface LhRentalService {
    int create(LhRentalVO vo);
    int create(LhRentalDTO dto);
    List<LhRentalDTO> getByPanId(String panId);
    LhRentalDTO getById(Integer id);
}
