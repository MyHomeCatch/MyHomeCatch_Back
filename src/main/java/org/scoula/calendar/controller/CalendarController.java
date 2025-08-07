package org.scoula.calendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.calendar.dto.CalendarResponseDTO;
import org.scoula.calendar.service.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@Log4j2
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<CalendarResponseDTO> getCalendar(@RequestParam String date) {
        return ResponseEntity.ok(calendarService.getCalendar(date));
    }
}
