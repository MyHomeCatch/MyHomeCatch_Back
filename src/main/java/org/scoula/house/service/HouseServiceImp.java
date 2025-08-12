package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.mapper.ApplyHomeAPTMapper;
import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.dto.HousePage.*;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.scoula.house.mapper.HouseFilterMapper;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.scoula.lh.danzi.domain.DanziApplyVO;
import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.danzi.dto.*;
import org.scoula.lh.danzi.dto.http.DanziRequestDTO;
import org.scoula.lh.danzi.dto.http.DanziResponseDTO;
import org.scoula.lh.danzi.mapper.DanziApplyMapper;
import org.scoula.lh.danzi.mapper.DanziAttMapper;
import org.scoula.lh.danzi.mapper.DanziMapper;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.scoula.selfCheck.mapper.SelfCheckMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class HouseServiceImp implements HouseService {

    private final LhRentalHouseMapper lhRentalHouseMapper;
    private final LhHousingHouseMapper lhHousingHouseMapper;
    private final LhNoticeMapper lhNoticeMapper;
    private final ApplyHomeOfficetelDetailMapper applyHomeOfficetelDetailMapper;
    private final HouseFilterMapper houseFilterMapper;
    private final DanziMapper danziMapper;
    private final DanziAttMapper danziAttMapper;
    private final DanziApplyMapper danziApplyMapper;
    private final ApplyHomeAPTMapper applyHomeAPTMapper;
    private final SelfCheckMapper selfCheckMapper;

    @Override
    public HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto) {
        // 필터 전처리와 지역 매핑을 한번에 처리
        requestDto.processFilters();

        List<HouseCardVO> voList = houseFilterMapper.getHousingList(requestDto);
        int totalCount = houseFilterMapper.getHousingCount(requestDto);

        List<HouseCardDTO> responseDtoList = voList.stream()
                .map(HouseCardDTO::ofHouseCardVO)
                .collect(Collectors.toList());

        PageInfoDTO pageInfo = PageInfoDTO.from(requestDto, totalCount);

        return HousePageResponseDTO.builder()
                .housingList(responseDtoList)
                .pageInfo(pageInfo)
                .build();
    }


    @Override
    public DanziResponseDTO getHouse(Integer danziId) {
        // prefix 사용해 청약홈 얻어오던 코드
//        String[] tokens = houseId.split("-");
//        String table = tokens[0];
//        String id = tokens[1];
//
//        if(table.equals("lhrental")) {
//            return HouseDTO.ofLhRentalHouseVO(lhRentalHouseMapper.get(Integer.parseInt(id)));
//        }
//
//        if(table.equals("lhhousing")) {
//            return HouseDTO.ofLhHousingHouseVO(lhHousingHouseMapper.get(Integer.parseInt(id)));
//        }
//
//        if(table.equals("ahOfficetel")) {
//            return HouseDTO.ofApplyHomeOfficetelHouseVO(applyHomeOfficetelDetailMapper.get(id));
//        }
//
//        if(table.equals("ahApt")) {
//            return HouseDTO.ofApplyHomeAPTPublicVO(applyHomeAPTMapper.getByHouseNo(id));
//        }

        DanziVO danziVO = danziMapper.findById(danziId);

        DanziDTO danziDTO = DanziDTO.builder()
                .danziId(danziVO.getDanziId())
                .bzdtNm(danziVO.getBzdtNm())
                .lctAraAdr(danziVO.getLctAraAdr())
                .lctAraDtlAdr(danziVO.getLctAraDtlAdr())
                .minMaxRsdnDdoAr(danziVO.getMinMaxRsdnDdoAr())
                .sumTotHshCnt(danziVO.getSumTotHshCnt())
                .htnFmlaDeCoNm(danziVO.getHtnFmlaDeCoNm())
                .mvinXpcYm(DanziDTO.DateParser.parseFullDate(danziVO.getMvinXpcYm()))
                .build();

        List<DanziApplyVO> danziApplyVOList = danziApplyMapper.findByDanziId(danziId);
        List<DanziApplyDTO> danziApplyDTOList = danziApplyVOList.stream()
                .map(DanziApplyDTO::from)
                .collect(Collectors.toList());

        List<DanziAttVO> danziAttVOList = danziAttMapper.getBydanziId(danziId);
        List<DanziAttDTO> danziAttDTOList = danziAttVOList.stream()
                .map(DanziAttDTO::toDanziAttDTO)
                .collect(Collectors.toList());

        List<NoticeInfoDTO> noticeInfoDTOList = danziMapper.findNoticesByDanziId(danziId);

        String selfCheckMatchResult = null;
        return DanziResponseDTO.builder()
                .danzi(danziDTO)
                .applies(danziApplyDTOList)
                .attachments(danziAttDTOList)
                .notices(noticeInfoDTOList)
                .selfCheckMatchResult(selfCheckMatchResult)
                .build();
    }


    @Override
    public DanziResponseDTO getHouseWithUserData(DanziRequestDTO requestDto, Integer houseId) {
        DanziResponseDTO responseDTO = getHouse(houseId);
        String selfCheckMatchResult = null;
        List<String> userSelfCheckResult = requestDto.getSelfCheckResult();

            HouseCardDTO houseCardDTO = getHouseCard(houseId);
            String noticeType = houseCardDTO.getNoticeType(); // 단지 정보에서 공고 유형을 가져옴

            if (userSelfCheckResult != null && noticeType != null) {
                Optional<String> matched = userSelfCheckResult.stream()
                        .filter(s -> s.contains(noticeType))
                        .findFirst();

                if (matched.isPresent()) {
                    selfCheckMatchResult = matched.get();
                    responseDTO.setSelfCheckMatchResult(selfCheckMatchResult);
                    log.info("selfCheckMatchResult: {}", selfCheckMatchResult);
                    log.info("noticeType: {}", noticeType);
                }
            }

        return responseDTO;
    }

    @Override
    public HouseCardDTO getHouseCard(Integer houseId) {
        Integer noticeId = lhNoticeMapper.getNoticeId(houseId);
        HouseCardDTO houseCardDTO = HouseCardDTO.ofHouseCardVO(houseFilterMapper.getHouseCard(houseId, noticeId));
        return houseCardDTO;
    }
}


