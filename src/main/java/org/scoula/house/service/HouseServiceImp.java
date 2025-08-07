package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.domain.ApplyHomeAptPublicVO;
import org.scoula.house.dto.HousePage.HouseDTO;
import org.scoula.house.mapper.ApplyHomeAPTMapper;
import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.dto.HousePage.*;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.scoula.house.mapper.HouseFilterMapper;
import org.scoula.house.mapper.LhHousingHouseMapper;
import org.scoula.house.mapper.LhRentalHouseMapper;
import org.scoula.house.util.RegionMapper;
import org.scoula.lh.danzi.domain.DanziApplyVO;
import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.danzi.dto.*;
import org.scoula.lh.danzi.mapper.DanziApplyMapper;
import org.scoula.lh.danzi.mapper.DanziAttMapper;
import org.scoula.lh.danzi.mapper.DanziMapper;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public HousePageResponseDTO getHouses(HouseSearchRequestDTO requestDto) {
        // DTO에 값이 있으면 자동으로 필터링됨
        requestDto.setCnpCdNm(RegionMapper.mapToFullRegion(requestDto.getCnpCdNm()));
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

        return DanziResponseDTO.builder()
                .danzi(danziDTO)
                .applies(danziApplyDTOList)
                .attachments(danziAttDTOList)
                .notices(noticeInfoDTOList)
                .build();
        }

    @Override
    public HouseCardDTO getHouseCard(Integer houseId) {
        int noticeId = lhNoticeMapper.getNoticeId(houseId);
        HouseCardDTO houseCardDTO = HouseCardDTO.ofHouseCardVO(houseFilterMapper.getHouseCard(houseId, noticeId));
        return houseCardDTO;
    }
}

    // 정정공고 boolean 용(Att update용)
//    public boolean isCorrectedNotice(String panId) {
//        if (panId == null) return false;
//        String panSs = noticeMapper.getPanSsByPanId(panId);
//        return "정정공고".equals(panSs);
//    }

