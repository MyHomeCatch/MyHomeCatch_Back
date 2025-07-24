package org.scoula.lh.service.lhRental;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;
import org.scoula.lh.mapper.lhRental.LhRentalAttMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LhRentalAttServiceImp implements LhRentalAttService {

    private final LhRentalAttMapper lhRentalAttMapper;

    @Override
    public int create(LhRentalAttDTO lhRentalAttDTO) {
        return lhRentalAttMapper.create(lhRentalAttDTO.toVO());
    }

    @Override
    public int create(LhRentalAttVO lhRentalAttVO) {
        return lhRentalAttMapper.create(lhRentalAttVO);
    }

    @Override
    public List<LhRentalAttDTO> getByPanId(String panId) {
        return lhRentalAttMapper.getByPanId(panId).stream().map(LhRentalAttDTO::of).toList();
    }

    @Override
    public LhRentalAttDTO getById(Integer id) {
        return LhRentalAttDTO.of(lhRentalAttMapper.getById(id));
    }
}
