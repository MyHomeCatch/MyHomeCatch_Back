package org.scoula.house.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HouseDTO;
import org.scoula.house.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house")
@Log4j2
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseDTO> getHouse(@PathVariable String houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }
}
