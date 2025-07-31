package org.scoula.lh.att.mapper;

import org.scoula.lh.att.domain.DanziAttVO;

import java.util.List;

public interface DanziAttMapper {
    int create(DanziAttVO danziAttVO);
    int createAll(List<DanziAttVO> danziAttVO);
    List<DanziAttVO> getBydanziId(Integer danziId);
}
