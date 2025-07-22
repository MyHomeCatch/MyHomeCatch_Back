package org.scoula.chapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.chapi.scheduler.CHOfficetelScheduler;
import org.scoula.chapi.service.OfficetelDBService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ch/officetel")
public class ApiController {
    private final OfficetelDBService officetelDbService;

    private final CHOfficetelScheduler officetelScheduler;

    @GetMapping("/fetch")
    public ResponseEntity<String> fetch() {
        log.info("Fetching officetel data");
        try{
            int affectedRows = officetelDbService.fetchAndInsertAPIData();
            String message = "Total "+affectedRows+" API data fetched successfully";
            log.info(message);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }

    @GetMapping("/fetch/model")
    public ResponseEntity<String> fetchModel() {
        log.info("Fetching officetel Model data");
        try{
            int affectedRows = officetelDbService.fetchOfficetelModel();
            String message = "Total "+affectedRows+" API data fetched successfully";
            log.info(message);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }

    @GetMapping("/fetch/cmpet")
    public ResponseEntity<String> fetchCmpet() {
        log.info("Fetching officetel Cmpet data");
        try{
            int affectedRows = officetelDbService.fetchOfficetelCmpet();
            String message = "Total "+affectedRows+" API data fetched successfully";
            log.info(message);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }

    @GetMapping("/schedule")
    public ResponseEntity<String> fetchAndUpdate() {
        log.info("Fetch and update officetel data by schedule");
        try{
            officetelScheduler.fetchAndUpdateNotices();
            log.info("Fetch and update data successfully");
            return ResponseEntity.ok("Fetch and update data successfully");
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }
}
