package org.scoula.chapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.service.DBService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ch/officetel")
public class ApiController {
    private final DBService dbService;

    @GetMapping("/fetchData")
    public ResponseEntity<String> fetchData() {
        log.info("Fetching officetel data");
        try{
            int affectedRows = dbService.fetchAndInsertAPIData();
            String message = "Total "+affectedRows+" API data fetched successfully";
            log.info(message);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }
}
