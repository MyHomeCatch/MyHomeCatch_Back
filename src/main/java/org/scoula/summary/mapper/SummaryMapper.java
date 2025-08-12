package org.scoula.summary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;

import java.util.List;

@Mapper
public interface SummaryMapper {

    String findByPanId(@Param("danziId") int danziId);

    void insertSummary(@Param("danziId") int danziId, @Param("summary") String summary);

    void insertSummaryV2(NoticeSummaryDTO summary);

    NoticeSummaryDTO findDTOByDanziId(@Param("danziId") int danziId);

}
