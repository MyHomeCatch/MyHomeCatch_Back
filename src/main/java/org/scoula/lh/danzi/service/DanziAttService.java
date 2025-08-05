package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.danzi.dto.DanziAttDTO;

import java.util.List;

public interface DanziAttService {
    int create(DanziAttVO danziAttVO);
    int createAll(List<DanziAttVO> danziAttVOList);
    List<DanziAttDTO> getDanziAttsByDanziId(String DanziId);

}
