package org.scoula.lh.danzi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.service.ThumbServiceImpl;
import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.danzi.dto.DanziAttDTO;
import org.scoula.lh.danzi.mapper.DanziAttMapper;
import org.scoula.lh.danzi.mapper.DanziMapper;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class DanziAttServiceImpl implements DanziAttService {

    private final ThumbServiceImpl thumbService;
    private final DanziServiceImpl danziService;
    private final DanziAttMapper mapper;
    private final LhNoticeMapper lhNoticeMapper;

    @Override
    public List<DanziAttDTO> getDanziAttsByDanziId(String DanziId) {
        return List.of();
    }

    @Override
    public int createAll(List<DanziAttVO> danziAttVOList) {
        List<DanziAttVO> returnVO = new ArrayList<>();

        for(DanziAttVO vo : danziAttVOList) {
            DanziAttVO forSaveVO= thumbService.createDanziThumbVO(vo);
            mapper.create(forSaveVO);
            returnVO.add(forSaveVO);
        }
        return returnVO.size();
    }

    @Override
    public int create(DanziAttVO danziAttVO) {
        DanziAttVO forSaveVO= thumbService.createDanziThumbVO(danziAttVO);
        return mapper.create(forSaveVO);
    }
}
