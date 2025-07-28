package org.scoula.house.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HouseDTO;
import org.scoula.house.service.HouseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/house")
@Log4j2
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("")
    public ResponseEntity<List<HouseDTO>> getHouses() {
        return ResponseEntity.ok(houseService.getHouses());
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseDTO> getHouse(@PathVariable String houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }
}
