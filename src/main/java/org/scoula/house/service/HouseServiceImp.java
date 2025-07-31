package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.domain.LhHousingApplyVO;
import org.scoula.house.domain.LhNoticeVO;
import org.scoula.house.domain.LhRentalApplyVO;
import org.scoula.house.dto.CalendarDTO;
import org.scoula.house.dto.HouseDTO;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhHouseNoticeMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class HouseServiceImp implements HouseService {

    private final LhRentalHouseMapper lhRentalHouseMapper;
    private final LhHousingHouseMapper lhHousingHouseMapper;
    private final ApplyHomeOfficetelDetailMapper applyHomeOfficetelDetailMapper;
    private final LhHouseNoticeMapper lhHouseNoticeMapper;

    @Override
    public List<HouseDTO> getHouses() {
        // 1. 데이터 조회 및 변환
        List<HouseDTO> rentalHouses = lhRentalHouseMapper.getRecentHouses().stream()
                .map(HouseDTO::ofLhRentalHouseVO)
                .toList();

        List<HouseDTO> housingHouses = lhHousingHouseMapper.getRecentHouses().stream()
                .map(HouseDTO::ofLhHousingHouseVO)
                .toList();

        // 2. 합치고 최신순 정렬
        return Stream.concat(rentalHouses.stream(), housingHouses.stream())
                .sorted(Comparator.comparing(HouseDTO::getNoticeDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    @Override
    public HouseDTO getHouse(String houseId) {
        String[] tokens = houseId.split("-");
        String table = tokens[0];
        String id = tokens[1];

        if(table.equals("lhrental")) {
            return HouseDTO.ofLhRentalHouseVO(lhRentalHouseMapper.get(Integer.parseInt(id)));
        }

        if(table.equals("lhhousing")) {
            return HouseDTO.ofLhHousingHouseVO(lhHousingHouseMapper.get(Integer.parseInt(id)));
        }

        if(table.equals("ahOfficetel")) {
            return HouseDTO.ofApplyHomeOfficetelHouseVO(applyHomeOfficetelDetailMapper.get(id));
        }

        return null;
    }

    @Override
    public List<CalendarDTO> getCalendar(String date) {
        List<CalendarDTO> calendars = new ArrayList<>();

        // 해당 년도-월 공고 조회
        List<LhHousingApplyVO> housingApplyVOList = lhHousingHouseMapper.getMonthHouses(date);
        List<LhRentalApplyVO> rentalApplyVOList = lhRentalHouseMapper.getMonthHouses(date);

        // 1. pan_id 리스트 수집
        Set<String> idSet = new HashSet<>();
        for (LhHousingApplyVO vo : housingApplyVOList) {
            idSet.add(vo.getPan_id());
        }
        for (LhRentalApplyVO vo : rentalApplyVOList) {
            idSet.add(vo.getPan_id());
        }

        // 2. 한 번에 조회
        List<LhNoticeVO> noticeList = lhHouseNoticeMapper.getMonthHouses(new ArrayList<>(idSet));

        // 3. pan_id로 매핑
        Map<String, LhNoticeVO> noticeMap = noticeList.stream()
                .collect(Collectors.toMap(LhNoticeVO::getPan_id, Function.identity()));

        // 4. CalendarDTO 생성
        for (LhHousingApplyVO vo : housingApplyVOList) {
            LhNoticeVO notice = noticeMap.get(vo.getPan_id());
            if (notice != null && idSet.contains(vo.getPan_id())) {
                String formattedDate = vo.getStart_dttm().split(" ")[0].replaceAll("-", ".");
                calendars.add(CalendarDTO.of(formattedDate, notice));
                idSet.remove(vo.getPan_id());
            }
        }

        for (LhRentalApplyVO vo : rentalApplyVOList) {
            LhNoticeVO notice = noticeMap.get(vo.getPan_id());
            if (notice != null && idSet.contains(vo.getPan_id())) {
                calendars.add(CalendarDTO.of(vo.getSbsc_acp_st_dt(), notice));
                idSet.remove(vo.getPan_id());
            }
        }

        return calendars;
    }
}
