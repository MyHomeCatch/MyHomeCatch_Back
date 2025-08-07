package org.scoula.lh.danzi.mapper;

import org.scoula.lh.danzi.domain.DanziAttVO;

import java.util.List;

public interface DanziAttMapper {
    int create(DanziAttVO danziAttVO);
    int createAll(List<DanziAttVO> danziAttVO);
    List<DanziAttVO> getBydanziId(Integer danziId);
}
