package org.scoula.summary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SummaryMapper {

    String findByPanId(@Param("danziId") int danziId);

    void insertSummary(@Param("danziId") int danziId, @Param("summary") String summary);
}
