package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.LhRentalHouseVO;
import org.scoula.house.util.DateParser;
import org.scoula.house.util.RegionMapper;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseDTO {

    /**
     * 공고번호 (apiApt-123123123), table 구분 prefix 삽입
     * ahApt-
     * ahOfficetel- 
     * lhhousing-
     * lhrental-
     */
    private String houseId;

    /**
     * 주택명
     */
    private String houseName;

    /**
     * 공고상세 URL
     */
    private String noticeUrl;

    /**
     * 공급규모(세대)
     */
    private Integer totalSupply;

    /**
     * 모집공고일
     */
    private Date noticeDate;

    /**
     * 청약접수시작일
     */
    private Date applyBeginDate;

    /**
     * 청약접수종료일
     */
    private Date applyEndDate;

    /**
     * 계약시작일
     */
    private Date contractBeginDate;

    /**
     * 계약종료일
     */
    private Date contractEndDate;

    /**
     * 당첨자발표일
     */
    private Date announceDate;

    /**
     * 입주예정월
     */
    private Date moveInMonth;

    /**
     * 지역 (LH는 2글자로 변환)
     */
    private String region;

    /**
     * 상세주소
     */
    private String address;

    /**
     * 분양금액(최고금액)
     */
    private Integer topAmount;

    /**
     * 카테고리1 (APT, 준주택)
     */
    private String houseType;

    /**
     * 카테고리2 (분양, 임대, 행복주택, 공공임대, 국민임대, ..)
     */
    private String supplyType;

    /**
     * 시행사
     */
    private String company;

    /**
     * 청약홈 APT 상세정보
     */
    private List<ApplyHomeAptDetailDTO> applyHomeAptDetails;

    /**
     * 청약홈 오피스텔 상세정보
     */
    private List<ApplyHomeOfficetelDetailDTO> applyHomeOfficetelDetails;

    /**
     * LH 분양건물상세정보
     */
    private List<LhHousingDetailDTO> lhHousingDetails;

    /**
     * LH 임대건물상세정보
     */
    private LhRentalDetailDTO lhRentalDetail;

    public static HouseDTO ofLhRentalHouseVO(LhRentalHouseVO vo) {
        LhRentalApplyVO apply = vo.getApply();
        LhNoticeVO notice = vo.getNotice();

        LhRentalDetailDTO lhRentalDetailDTO = LhRentalDetailDTO.builder()
                .notice(NoticeDTO.of(vo.getNotice()))
                .lhRentalAtts(vo.getLhRentalAttList().stream().map(LhRentalAttDTO::of).toList())
                .noticeAtts(vo.getNoticeAttList().stream().map(NoticeAttDTO::of).toList())
                .build();

        return HouseDTO.builder()
                .houseId("lhrental-" + vo.getLhRentalId())
                .houseName(vo.getLccNtNm())
                .noticeUrl(notice != null ? notice.getDtlUrl() : null)
                .totalSupply(Integer.parseInt(vo.getHshCnt()))
                .noticeDate(null)
                .applyBeginDate(apply != null ? DateParser.parseDate(apply.getSbscAcpStDt()) : null)
                .applyEndDate(apply != null ? DateParser.parseDate(apply.getSbscAcpClsgDt()) : null)
                .contractBeginDate(apply != null ? apply.getCtrtStDt() : null)
                .contractEndDate(apply != null ? apply.getCtrtEdDt() : null)
                .announceDate(apply != null ? apply.getPzwrAncDt() : null)
                .moveInMonth(DateParser.parseDate(vo.getMvinXpcYm()))
                .region(notice != null ? RegionMapper.mapToShortRegion(notice.getCnpCdNm()) : "기타")
                .address(vo.getLgdnAdr())
                .houseType("APT")
                .supplyType("APT")
                .company("LH")
                .lhRentalDetails(null)
                .build();
    }
}
