package org.scoula.lh.service.lhHousing;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.dto.lhHousing.LhHousingDTO;
import org.scoula.lh.mapper.lhHousing.LhHousingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LhHousingServiceImp implements LhHousingService {

    private final LhHousingMapper housingMapper;

    @Override
    public List<LhHousingDTO> getByPanId(String panId) {
        return housingMapper.getByPanId(panId).stream().map(LhHousingDTO::of).toList();
    }

    @Override
    public int create(DanziVO dto) {
        return 1;
        //return housingMapper.create(dto.toVO());
    }

    @Override
    public int createAll(List<LhHousingDTO> dtos) {
        return housingMapper.createAll(dtos.stream().map(LhHousingDTO::toVO).toList());
    }
}
