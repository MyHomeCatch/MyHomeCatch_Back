package org.scoula.lh.service.lhHousing;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.scoula.lh.mapper.lhHousing.LhHousingAttMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class LhHousingAttServiceImp implements LhHousingAttService {

    private final LhHousingAttMapper housingAttMapper;

    @Override
    public int create(LhHousingAttVO housingAttVO) {
        return housingAttMapper.create(housingAttVO);
    }

    @Override
    public int create(LhHousingAttDTO housingAttDTO) {
        return housingAttMapper.create(housingAttDTO.toVO());
    }

    @Override
    public List<LhHousingAttDTO> getByPanId(String panId) {
        return housingAttMapper.getByPanId(panId).stream().map(LhHousingAttDTO::of).toList();
    }

    @Override
    public LhHousingAttDTO getById(Integer lhHousingAttId) {
        return LhHousingAttDTO.of(housingAttMapper.getById(lhHousingAttId));
    }

    @Override
    public List<LhHousingAttDTO> getByBzdtNm(String bzdtNm) {
        return housingAttMapper.getByBzdtNm(bzdtNm).stream().map(LhHousingAttDTO::of).toList();
    }
}
