package org.scoula.house.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.HousePage.HouseCardDTO;
import org.scoula.house.dto.HousePage.HousePageResponseDTO;
import org.scoula.house.dto.HousePage.HouseSearchRequestDTO;
import org.scoula.house.service.HouseService;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.danzi.dto.http.DanziRequestDTO;
import org.scoula.lh.danzi.dto.http.DanziResponseDTO;
import org.scoula.lh.danzi.dto.http.PersonalizedCardDTO;
import org.scoula.lh.danzi.service.PersonalizedService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/house")
@Log4j2
@RequiredArgsConstructor
@Api(tags = "House API")
public class HouseController {

    private final HouseService houseService;
    private final PersonalizedService personalizedService;

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

    @PostMapping(value = "/{houseId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get House with User Data", notes = "Get details of a specific house with user data.")
    public ResponseEntity<?> getHouseWithUserData(@RequestBody DanziRequestDTO requestDto, @PathVariable Integer houseId) {
        DanziResponseDTO house = houseService.getHouseWithUserData(requestDto, houseId);
        PersonalizedCardDTO card = personalizedService.getOrCreatePersonalCard(houseId, requestDto.getUserId());
        return ResponseEntity.ok(Map.of("house", house, "personal_card", card));
    }

    @PostMapping(value = "/json/{houseId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get House with Json Parsing User Data", notes = "Get details of a specific house with user data.")
    public ResponseEntity<?> getHouseWithUserDataJson(@RequestBody DanziRequestDTO requestDto, @PathVariable Integer houseId) {
        DanziResponseDTO house = houseService.getHouseWithUserData(requestDto, houseId);
        PersonalizedCardDTO card = personalizedService.getOrCreatePersonalCardFromJson(houseId, requestDto.getUserId());
        return ResponseEntity.ok(Map.of("house", house, "personal_card", card));
    }

    @GetMapping("/card/{houseId}")
    @ApiOperation(value = "Get House Card", notes = "Get a card representation of a specific house.")
    public ResponseEntity<HouseCardDTO> getHouseCard(@PathVariable Integer houseId) {
        return ResponseEntity.ok(houseService.getHouseCard(houseId));
    }
}
