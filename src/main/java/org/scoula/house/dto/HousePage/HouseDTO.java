package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.ApplyHomeAptPublicVO;
import org.scoula.house.domain.ApplyHomeOfficetelHouseVO;
import org.scoula.house.domain.LhHousingHouseVO;
import org.scoula.house.domain.LhRentalHouseVO;
import org.scoula.house.dto.ApplyHomeAptDetailDTO;
import org.scoula.house.dto.ApplyHomeOfficetelDetailDTO;
import org.scoula.house.dto.LhHousingDetailDTO;
import org.scoula.house.dto.LhRentalDetailDTO;
import org.scoula.house.util.DateParser;
import org.scoula.house.util.DateRangeParser;
import org.scoula.house.util.RegionMapper;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.domain.NoticeAttVO;
import org.scoula.lh.domain.housing.LhHousingApplyVO;
import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.dto.lhHousing.LhHousingApplyDTO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;

import java.util.Date;
import java.util.List;

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
    private List<ApplyHomeAptPublicDTO> applyHomeAptDetails;

    /**
     * 청약홈 오피스텔 상세정보
     */
    private List<ApplyHomeOfficetelDetailDTO> applyHomeOfficetelDetails;

    /**
     * LH 분양건물상세정보
     */
    private LhHousingDetailDTO lhHousingDetail;

    /**
     * LH 임대건물상세정보
     */
    private LhRentalDetailDTO lhRentalDetail;

    public static HouseDTO ofLhHousingHouseVO(LhHousingHouseVO vo) {
        LhNoticeVO notice = vo.getNotice();
        List<LhHousingApplyVO> applyList = vo.getApplyList();

        LhHousingDetailDTO lhHousingDetail = LhHousingDetailDTO.builder()
                .notice(NoticeDTO.of(vo.getNotice()))
                .applyList(vo.getApplyList().stream().map(LhHousingApplyDTO::of).toList())
                .lhHousingAttList(vo.getHousingAttList().stream().map(LhHousingAttDTO::of).toList())
                .noticeAttList(vo.getNoticeAttList().stream().map(NoticeAttDTO::of).toList())
                .build();

        return HouseDTO.builder()
                .houseId("lhhousing-" + vo.getLhHousingId())
                .houseName(vo.getBzdtNm())
                .noticeUrl(notice != null ? notice.getDtlUrl() : null)
                .totalSupply(Integer.parseInt(vo.getSumTotHshCnt()))
                .noticeDate(notice != null ? vo.getNotice().getPanNtStDt(): null)
                .applyBeginDate(applyList != null ? DateRangeParser.parseStartDate(applyList.get(0).getAcpDttm()) : null)
                .applyEndDate(applyList != null ? DateRangeParser.parseEndDate(applyList.get(0).getAcpDttm()) : null)
                .contractBeginDate(applyList != null ? applyList.get(0).getCtrtStDt() : null)
                .contractEndDate(applyList != null ? applyList.get(0).getCtrtEdDt() : null)
                .announceDate(applyList != null ? applyList.get(0).getPzwrAncDt() : null)
                .moveInMonth(DateParser.parseDate(vo.getMvinXpcYm()))
                .region(notice != null ? RegionMapper.mapToShortRegion(notice.getCnpCdNm()) : "기타")
                .address(vo.getLctAraAdr())
                .houseType("APT")
                .supplyType(notice != null ? notice.getAisTpCdNm() : null)
                .company("LH")
                .lhHousingDetail(lhHousingDetail)
                .build();
    }

    public static HouseDTO ofLhRentalHouseVO(LhRentalHouseVO vo) {
        List<LhRentalApplyVO> applyList = vo.getApply();
        LhNoticeVO notice = vo.getNotice();
        List<LhRentalAttVO> lhRentalAttList = vo.getLhRentalAttList();
        List<NoticeAttVO> noticeAttList = vo.getNoticeAttList();

        LhRentalDetailDTO lhRentalDetailDTO = LhRentalDetailDTO.builder()
                .notice(NoticeDTO.of(vo.getNotice()))
                .lhRentalAtts(lhRentalAttList != null ? vo.getLhRentalAttList().stream().map(LhRentalAttDTO::of).toList(): null)
                .noticeAtts(noticeAttList != null ? vo.getNoticeAttList().stream().map(NoticeAttDTO::of).toList() : null)
                .build();

        return HouseDTO.builder()
                .houseId("lhrental-" + vo.getLhRentalId())
                .houseName(vo.getLccNtNm())
                .noticeUrl(notice != null ? notice.getDtlUrl() : null)
                .totalSupply(Integer.parseInt(vo.getHshCnt()))
                .noticeDate(notice != null ? vo.getNotice().getPanNtStDt(): null)
                .applyBeginDate(applyList != null && !applyList.isEmpty() ? DateParser.parseDate(applyList.get(0).getSbscAcpStDt()) : null)
                .applyEndDate(applyList != null && !applyList.isEmpty() ? DateParser.parseDate(applyList.get(0).getSbscAcpClsgDt()) : null)
                .contractBeginDate(applyList != null && !applyList.isEmpty() ? applyList.get(0).getCtrtStDt() : null)
                .contractEndDate(applyList != null && !applyList.isEmpty() ? applyList.get(0).getCtrtEdDt() : null)
                .announceDate(applyList != null && !applyList.isEmpty() ? applyList.get(0).getPzwrAncDt() : null)
                .moveInMonth(DateParser.parseDate(vo.getMvinXpcYm()))
                .region(notice != null ? RegionMapper.mapToShortRegion(notice.getCnpCdNm()) : "기타")
                .address(vo.getLgdnAdr())
                .houseType("APT")
                .supplyType(notice != null ? notice.getAisTpCdNm() : null)
                .company("LH")
                .lhRentalDetail(lhRentalDetailDTO)
                .build();
    }

    public static HouseDTO ofApplyHomeOfficetelHouseVO(ApplyHomeOfficetelHouseVO vo) {
        return HouseDTO.builder()
                .houseId("ahOfficetel-"+vo.getPblancNo())
                .houseName(vo.getHouseNm())
                .noticeUrl(vo.getPblancUrl())
                .totalSupply(vo.getTotSuplyHshldco())
                .noticeDate(vo.getRcritPblancDe())
                .applyBeginDate(vo.getSubscrptRceptBgnde())
                .applyEndDate(vo.getSubscrptRceptEndde())
                .contractBeginDate(vo.getCntrctCnclsBgnde())
                .contractEndDate(vo.getCntrctCnclsEndde())
                .announceDate(vo.getPrzwnerPresnatnDe())
                .moveInMonth(DateParser.parseDate(vo.getMvnPrearngeYm()))
                .region(vo.getSubscrptAreaCodeNm())
                .address(vo.getHssplyAdres())
                .houseType("준주택")
                .supplyType(vo.getHouseSecdNm())
                .company(vo.getBsnsMbyNm())
                .applyHomeOfficetelDetails(vo.getDetails())
                .build();
    }

    public static HouseDTO ofApplyHomeAPTPublicVO(ApplyHomeAptPublicVO vo) {
        if (vo == null) return null;

        return HouseDTO.builder()
                .houseId(vo.getPblancNo() != null ? "ahApt-" + vo.getPblancNo() : null)
                .houseName(vo.getHouseNm())
                .noticeUrl(vo.getHmpgAdres())
                .totalSupply(vo.getTotSuplyHshldco() != null ? vo.getTotSuplyHshldco() : 0)
                .noticeDate(vo.getRcritPblancDe() != null ? vo.getRcritPblancDe() : null)
                .applyBeginDate(vo.getSpsplyRceptBgnde() != null ? vo.getSpsplyRceptBgnde() : null)
                .applyEndDate(vo.getSpsplyRceptEgnde() != null ? vo.getSpsplyRceptEgnde() : null)
                .contractBeginDate(vo.getCntrctCnclsBgnde())
                .contractEndDate(vo.getCntrctCnclsEndde())
                .announceDate(vo.getPrzwnerPresnatnDe())
                .moveInMonth(DateParser.parseDate(vo.getMvnPrearngeYm()))
                .region(vo.getSubscrptAreaCodeNm() != null ? RegionMapper.mapToShortRegion(vo.getSubscrptAreaCodeNm()) : "기타")
                .address(vo.getHssplyAdres())
                .houseType(vo.getRentSecdNm())
                .supplyType(vo.getHouseSecdNm() != null ? vo.getHouseSecdNm() : "미정")
                .company(vo.getBsnsMbyNm() != null ? vo.getBsnsMbyNm() : "기타")
                .build();
    }
}
