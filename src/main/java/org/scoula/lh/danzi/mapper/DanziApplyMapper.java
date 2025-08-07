package org.scoula.lh.danzi.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.domain.DanziApplyVO;

import java.util.List;

public interface DanziApplyMapper {

    void insert(@Param("list") List<DanziApplyVO> list);
    List<DanziApplyVO> findByDanziId(int danziId);
    void updateByDanziId(@Param("list") List<String> list);
}