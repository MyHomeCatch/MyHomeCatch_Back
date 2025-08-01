package org.scoula.lh.danzi.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.domain.DanziNoticeVO;
import org.scoula.lh.danzi.domain.DanziVO;

import java.util.List;

public interface DanziMapper {
    int insert(@Param("list") List<DanziVO> list);
    void insertDanziNotice(DanziNoticeVO vo);
}
