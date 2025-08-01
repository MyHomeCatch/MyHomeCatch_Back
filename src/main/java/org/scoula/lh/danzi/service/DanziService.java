package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.domain.DanziNoticeVO;
import org.scoula.lh.danzi.domain.DanziVO;

import java.util.List;

public interface DanziService {

    int create(DanziVO vo);
    List<DanziVO> createAll(List<DanziVO> voList);
    void createDanziNotice(DanziNoticeVO vo);
    boolean isCorrectedNoticeByDanziId(Integer danziId);
    void updateMissingNoticeMapping(String panId, int danziId);
}
