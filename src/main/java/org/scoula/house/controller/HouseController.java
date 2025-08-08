package org.scoula.house.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HousePage.HouseCardDTO;
import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.dto.HousePage.HousePageResponseDTO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;
import org.scoula.house.service.HouseService;
import org.scoula.lh.danzi.dto.DanziRequestDTO;
import org.scoula.lh.danzi.dto.DanziResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.scoula.user.domain.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/house")
@Log4j2
@RequiredArgsConstructor
@Api(tags = "House API")
public class HouseController {

    private final HouseService houseService;

    @GetMapping("")
    @ApiOperation(value = "Get Houses", notes = "Get a paginated list of houses.")
    public ResponseEntity<HousePageResponseDTO> getHouses(HouseSearchRequestDTO requestDto) {
        return ResponseEntity.ok(houseService.getHouses(requestDto));
    }

    @GetMapping("/{houseId}")
    @ApiOperation(value = "Get House", notes = "Get details of a specific house.")
    public ResponseEntity<DanziResponseDTO> getHouse(@PathVariable Integer houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @PostMapping("/{houseId}")
    @ApiOperation(value = "Get House with User Data", notes = "Get details of a specific house with user data.")
    public ResponseEntity<DanziResponseDTO> getHouseWithUserData(@RequestBody DanziRequestDTO requestDto, @PathVariable Integer houseId) {
        return ResponseEntity.ok(houseService.getHouseWithUserData(requestDto, houseId));

    }

    @GetMapping("/card/{houseId}")
    @ApiOperation(value = "Get House Card", notes = "Get a card representation of a specific house.")
    public ResponseEntity<HouseCardDTO> getHouseCard(@PathVariable Integer houseId) {
        return ResponseEntity.ok(houseService.getHouseCard(houseId));
    }
}
