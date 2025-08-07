package org.scoula.calendar.service;

import org.scoula.calendar.dto.CalendarResponseDTO;

import java.util.List;

public interface CalendarService {

    CalendarResponseDTO getCalendar(String date);
}
