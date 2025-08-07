package org.scoula.applyHome.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.applyHome.domain.ApplyHomeAnalysisVO;
import org.scoula.applyHome.domain.ApplyHomeSpecialVO;
import org.scoula.applyHome.domain.ApplyHomeVO;
import org.scoula.applyHome.dto.ApplyHomeAnalysisDTO;
import org.scoula.applyHome.dto.ApplyHomeDTO;
import org.scoula.applyHome.dto.ApplyHomeSpecialDTO;
import org.scoula.applyHome.mapper.ApplyHomeMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@Service
@Primary
@RequiredArgsConstructor
public class ApplyHomeServiceImpl implements ApplyHomeService {

    final private ApplyHomeMapper mapper;

    @Override
    public int create(ApplyHomeDTO dto) {
        ApplyHomeVO vo = dto.toVO();
        return mapper.create(vo);
    }

    @Override
    public int createAnalysis(ApplyHomeAnalysisDTO dto){
        ApplyHomeAnalysisVO vo = ApplyHomeAnalysisDTO.toVO(dto);
        return mapper.createAnalysis(vo);
    }

    @Override
    public int createSpecial(ApplyHomeSpecialDTO dto){
        ApplyHomeSpecialVO vo = ApplyHomeSpecialDTO.toVO(dto);
        return mapper.createSpecial(vo);
    }


}
