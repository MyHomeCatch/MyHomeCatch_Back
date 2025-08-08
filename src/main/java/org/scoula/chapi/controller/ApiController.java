package org.scoula.chapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "CH Officetel API")
public class ApiController {
    private final OfficetelDBService officetelDbService;

    private final CHOfficetelScheduler officetelScheduler;

    @GetMapping("/fetch")
    @ApiOperation(value = "Fetch Officetel Data", notes = "Fetch and insert officetel API data.")
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
    @ApiOperation(value = "Fetch Officetel Model Data", notes = "Fetch and insert officetel model data.")
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
    @ApiOperation(value = "Fetch Officetel Competition Data", notes = "Fetch and insert officetel competition data.")
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
    @ApiOperation(value = "Fetch and Update Officetel Data", notes = "Fetch and update all officetel data by schedule.")
    public ResponseEntity<String> fetchAndUpdate() {
        log.info("Fetch and update officetel data by schedule");
        try{
            officetelScheduler.fetchAndUpdateAllOfficetelData();
            log.info("Fetch and update data successfully");
            return ResponseEntity.ok("Fetch and update data successfully");
        } catch (RuntimeException e) {
            log.error("Error occured: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured: "+e.getMessage());
        }
    }
}
