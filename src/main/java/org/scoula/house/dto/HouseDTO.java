package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<LhRentalDetailDTO> lhRentalDetails;
}
