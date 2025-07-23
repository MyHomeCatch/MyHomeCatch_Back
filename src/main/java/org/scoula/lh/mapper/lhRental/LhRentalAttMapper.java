package org.scoula.lh.mapper.lhRental;

import org.scoula.lh.domain.rental.LhRentalAttVO;

import java.util.List;

public interface LhRentalAttMapper {
    int create(LhRentalAttVO lhRentalAttVO);
    List<LhRentalAttVO> getByPanId(String panId);
    LhRentalAttVO getById(Integer id);
}
