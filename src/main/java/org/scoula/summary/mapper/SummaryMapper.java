package org.scoula.summary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SummaryMapper {

    String findByPanId(@Param("panId") String panId);

    void insertSummary(@Param("panId") String panId, @Param("summary") String summary);
}
