package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.dto.CHOfficetelDTO;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplyHomeOfficetelTotalDTO {
    // 오피스텔공고 기본정보(주택공고)
    /**
     * 사업주체명
     */
    private String bsnsMbyNm;

    /**
     * 계약체결시작일
     */
    private Date cntrctCnclsBgnde;

    /**
     * 계약체결종료일
     */
    private Date cntrctCnclsEndde;

    /**
     * 홈페이지주소
     */
    private String hmpgAdres;

    /**
     * 주택상세구분코드
     */
    private String houseDtlSecd;

    /**
     * 주택상세구분코드명
     */
    private String houseDtlSecdNm;

    /**
     * 주택관리번호 (NOT NULL)
     */
    private String houseManageNo;

    /**
     * 주택명 (NOT NULL)
     */
    private String houseNm;

    /**
     * 주택구분코드
     */
    private String houseSecd;

    /**
     * 주택구분코드명
     */
    private String houseSecdNm;

    /**
     * 공급위치
     */
    private String hssplyAdres;

    /**
     * 공급위치우편번호
     */
    private String hssplyZip;

    /**
     * 문의처연락처
     */
    private String mdhsTelno;

    /**
     * 입주예정월
     */
    private String mvnPrearngeYm;

    /**
     * 뉴스명 (NULL 허용)
     */
    private String nsprcNm;

    /**
     * 공고번호 (PRIMARY KEY, NOT NULL)
     */
    private String pblancNo;

    /**
     * 모집공고URL
     */
    private String pblancUrl;

    /**
     * 당첨자발표일
     */
    private Date przwnerPresnatnDe;

    /**
     * 모집공고일
     */
    private Date rcritPblancDe;

    /**
     * 검색주택구분코드
     */
    private String searchHouseSecd;

    /**
     * 공급지역코드
     */
    private String subscrptAreaCode;

    /**
     * 공급지역명
     */
    private String subscrptAreaCodeNm;

    /**
     * 청약접수시작일
     */
    private Date subscrptRceptBgnde;

    /**
     * 청약접수종료일
     */
    private Date subscrptRceptEndde;

    /**
     * 총공급세대수
     */
    private int totSuplyHshldco;

    private List<ApplyHomeOfficetelDetailDTO> details;
}
