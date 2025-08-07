package org.scoula.lh.danzi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.DanziApplyVO;
import org.scoula.lh.danzi.mapper.DanziApplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DanziApplyServiceImpl implements DanziApplyService{

    private final DanziApplyMapper applyMapper;
    @Override
    public void createAll(List<DanziApplyVO> list) {
        applyMapper.insert(list);
    }
}
