package org.scoula.calendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.calendar.domain.CalendarVO;
import org.scoula.calendar.dto.CalendarDTO;
import org.scoula.calendar.dto.CalendarResponseDTO;
import org.scoula.calendar.mapper.CalendarMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarMapper calendarMapper;

    @Override
    public CalendarResponseDTO getCalendar(String date) {

        List<CalendarDTO> calendarList = calendarMapper.getCalendar(date).stream()
                .map(vo -> CalendarDTO.of(vo))
                .collect(Collectors.toList());

        return CalendarResponseDTO.builder()
                .totalCount(calendarList.size())
                .dataList(calendarList)
                .build();
    }
}
