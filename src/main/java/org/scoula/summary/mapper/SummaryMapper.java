package org.scoula.summary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;

import java.util.List;

@Mapper
public interface SummaryMapper {

    String findByPanId(@Param("danziId") int danziId);

    void insertSummary(@Param("danziId") int danziId, @Param("summary") String summary);

    void insertSummaryV2(NoticeSummaryDTO summary);

    void insertSummaryJsonDTO(JsonSummaryDTO summary);

    void insertSummaryJson(@Param("danziId") int danziId, @Param("summary") String summary);

    NoticeSummaryDTO findDTOByDanziId(@Param("danziId") int danziId);

    String findJsonByDanziId(@Param("danziId") int danziId);

    JsonSummaryDTO findJsonDTOByDanziId(@Param("danziId") int danziId);

}
