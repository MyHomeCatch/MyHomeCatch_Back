package org.scoula.lh.service.lhRental;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.dto.lhRental.LhRentalApplyDTO;
import org.scoula.lh.mapper.lhRental.LhRentalApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LhRentalApplyServiceImp implements LhRentalApplyService {

    private final LhRentalApplyMapper lhRentalApplyMapper;

    @Override
    public LhRentalApplyDTO getById(Integer id) {
        return LhRentalApplyDTO.of(lhRentalApplyMapper.getById(id));
    }

    @Override
    public List<LhRentalApplyDTO> getByPanId(String panId) {
        return lhRentalApplyMapper.getByPanId(panId).stream().map(LhRentalApplyDTO::of).toList();
    }

    @Override
    public int create(LhRentalApplyDTO lhRentalApplyDTO) {
        return lhRentalApplyMapper.create(lhRentalApplyDTO.toVO());
    }

    @Override
    public int create(LhRentalApplyVO lhRentalApplyVO) {
        return lhRentalApplyMapper.create(lhRentalApplyVO);
    }
}
