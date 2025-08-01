package org.scoula.lh.danzi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.DanziNoticeVO;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.danzi.mapper.DanziMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DanziServiceImpl implements DanziService {

    private final DanziMapper danziMapper;

    @Override
    public int create(DanziVO vo) {
        return 0;
    }

    @Override
    public List<DanziVO> createAll(List<DanziVO> voList) {
        int cnt = danziMapper.insert(voList);
        if(voList.size() == cnt)
            return voList;
        return null; // insert된 행과 추가하려는 데이터 수 다름(에러처리)
    }

    @Override
    public void createDanziNotice(DanziNoticeVO vo) {
        danziMapper.insertDanziNotice(vo);
    }
}
