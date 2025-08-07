package org.scoula.lh.mapper.lhRental;

import org.scoula.lh.domain.rental.LhRentalApplyVO;

import java.util.List;

public interface LhRentalApplyMapper {
    List<LhRentalApplyVO> getByPanId(String panId);
    LhRentalApplyVO getById(Integer id);
    int create(LhRentalApplyVO lhRentalApplyVO);
}
