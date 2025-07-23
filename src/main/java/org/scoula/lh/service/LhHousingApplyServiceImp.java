package org.scoula.lh.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.LhHousingApplyVO;
import org.scoula.lh.dto.LhHousingApplyDTO;
import org.scoula.lh.mapper.LhHousingApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LhHousingApplyServiceImp implements LhHousingApplyService {

    private final LhHousingApplyMapper housingApplyMapper;

    @Override
    public List<LhHousingApplyDTO> getAllByPanId(String panId) {
        return housingApplyMapper.getByPanId(panId).stream().map(LhHousingApplyDTO::of).toList();
    }

    @Override
    public int create(LhHousingApplyDTO housingApplyDTO) {
        return housingApplyMapper.create(housingApplyDTO.toVO());
    }

    @Override
    public int create(LhHousingApplyVO housingApplyVO) {
        return housingApplyMapper.create(housingApplyVO);
    }

    @Override
    public int createAll(List<LhHousingApplyDTO> housingApplyDTOs) {
        return housingApplyMapper.createAll(housingApplyDTOs.stream().map(LhHousingApplyDTO::toVO).toList());
    }
}
