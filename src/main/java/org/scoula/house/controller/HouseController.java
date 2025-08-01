package org.scoula.house.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.dto.HousePage.HousePageResponseDTO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;
import org.scoula.house.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/house")
@Log4j2
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("")
    public ResponseEntity<HousePageResponseDTO> getHouses(HouseSearchRequestDTO requestDto) {
        return ResponseEntity.ok(houseService.getHouses(requestDto));
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseDTO> getHouse(@PathVariable String houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }
}
