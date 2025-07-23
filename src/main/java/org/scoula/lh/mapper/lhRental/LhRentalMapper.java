package org.scoula.lh.mapper.lhRental;

import org.scoula.lh.domain.rental.LhRentalVO;

import java.util.List;

public interface LhRentalMapper {
    int create(LhRentalVO vo);
    int createAll(List<LhRentalVO> vos);
    LhRentalVO getById(Integer id);
    List<LhRentalVO> getByPanId(String panId);
}
