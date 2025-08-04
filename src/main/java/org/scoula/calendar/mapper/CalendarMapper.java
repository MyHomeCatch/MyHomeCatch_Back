package org.scoula.calendar.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.calendar.domain.CalendarVO;

import java.util.List;

public interface CalendarMapper {

    List<CalendarVO> getCalendar(@Param("date") String date);
}
