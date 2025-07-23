package org.scoula.lh.service.lhRental;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.rental.LhRentalVO;
import org.scoula.lh.dto.lhRental.LhRentalDTO;
import org.scoula.lh.mapper.lhRental.LhRentalMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LhRentalServiceImp implements LhRentalService {

    private final LhRentalMapper lhRentalMapper;

    @Override
    public int create(LhRentalVO vo) {
        return lhRentalMapper.create(vo);
    }

    @Override
    public int create(LhRentalDTO dto) {
        return lhRentalMapper.create(dto.toVO());
    }

    @Override
    public List<LhRentalDTO> getByPanId(String panId) {
        return lhRentalMapper.getByPanId(panId).stream().map(LhRentalDTO::of).toList();
    }

    @Override
    public LhRentalDTO getById(Integer id) {
        return LhRentalDTO.of(lhRentalMapper.getById(id));
    }
}
