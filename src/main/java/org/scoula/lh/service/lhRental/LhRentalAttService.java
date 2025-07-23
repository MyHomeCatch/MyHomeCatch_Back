package org.scoula.lh.service.lhRental;

import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;

import java.util.List;

public interface LhRentalAttService {
    int create(LhRentalAttDTO lhRentalAttDTO);
    int create(LhRentalAttVO lhRentalAttVO);
    List<LhRentalAttDTO> getByPanId(String panId);
    LhRentalAttDTO getById(Integer id);
}
