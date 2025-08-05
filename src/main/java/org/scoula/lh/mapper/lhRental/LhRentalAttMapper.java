package org.scoula.lh.mapper.lhRental;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.lh.domain.rental.LhRentalAttVO;

import java.util.List;

@Mapper
public interface LhRentalAttMapper {
    int create(LhRentalAttVO lhRentalAttVO);
    List<LhRentalAttVO> getByPanId(String panId);
    List<LhRentalAttVO> getAll();
    LhRentalAttVO getById(Integer id);
}
