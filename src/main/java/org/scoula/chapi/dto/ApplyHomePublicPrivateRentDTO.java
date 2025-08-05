package org.scoula.chapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.domain.ApplyHomePublicPrivateRentVO;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplyHomePublicPrivateRentDTO {
    @JsonProperty("BSNS_MBY_NM")
    private String bsnsMbyNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("CNTRCT_CNCLS_BGNDE")
    private Date cntrctCnclsBgnde;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("CNTRCT_CNCLS_ENDDE")
    private Date cntrctCnclsEndde;

    @JsonProperty("HMPG_ADRES")
    private String hmpgAdres;

    @JsonProperty("HOUSE_DETAIL_SECD")
    private String houseDetailSecd;

    @JsonProperty("HOUSE_DETAIL_SECD_NM")
    private String houseDetailSecdNm;

    @JsonProperty("HOUSE_MANAGE_NO")
    private String houseManageNo;

    @JsonProperty("HOUSE_NM")
    private String houseNm;

    @JsonProperty("HOUSE_SECD")
    private String houseSecd;

    @JsonProperty("HOUSE_SECD_NM")
    private String houseSecdNm;

    @JsonProperty("HSSPLY_ADRES")
    private String hssplyAdres;

    @JsonProperty("HSSPLY_ZIP")
    private String hssplyZip;

    @JsonProperty("MDHS_TELNO")
    private String mdhsTelno;

    @JsonProperty("MVN_PREARNGE_YM")
    private String mvnPrearngeYm;

    @JsonProperty("NSPRC_NM")
    private String nsprcNm;

    @JsonProperty("PBLANC_NO")
    private String pblancNo;

    @JsonProperty("PBLANC_URL")
    private String pblancUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("PRZWNER_PRESNATN_DE")
    private Date przwnerPresnatnDe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("RCRIT_PBLANC_DE")
    private Date rcritPblancDe;

    @JsonProperty("SEARCH_HOUSE_SECD")
    private String searchHouseSecd;

    @JsonProperty("SUBSCRPT_AREA_CODE")
    private String subscrptAreaCode;

    @JsonProperty("SUBSCRPT_AREA_CODE_NM")
    private String subscrptAreaCodeNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("SUBSCRPT_RCEPT_BGNDE")
    private Date subscrptRceptBgnde;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @JsonProperty("SUBSCRPT_RCEPT_ENDDE")
    private Date subscrptRceptEndde;

    @JsonProperty("TOT_SUPLY_HSHLDCO")
    private int totSuplyHshldco;

    public static ApplyHomePublicPrivateRentDTO of(ApplyHomePublicPrivateRentVO vo){
        return ApplyHomePublicPrivateRentDTO.builder()
                .bsnsMbyNm(vo.getBsnsMbyNm())
                .cntrctCnclsBgnde(vo.getCntrctCnclsBgnde())
                .cntrctCnclsEndde(vo.getCntrctCnclsEndde())
                .hmpgAdres(vo.getHmpgAdres())
                .houseDetailSecd(vo.getHouseDetailSecd())
                .houseDetailSecdNm(vo.getHouseDetailSecdNm())
                .houseManageNo(vo.getHouseManageNo())
                .houseNm(vo.getHouseNm())
                .houseSecd(vo.getHouseSecd())
                .houseSecdNm(vo.getHouseSecdNm())
                .hssplyAdres(vo.getHssplyAdres())
                .hssplyZip(vo.getHssplyZip())
                .mdhsTelno(vo.getMdhsTelno())
                .mvnPrearngeYm(vo.getMvnPrearngeYm())
                .nsprcNm(vo.getNsprcNm())
                .pblancNo(vo.getPblancNo())
                .pblancUrl(vo.getPblancUrl())
                .przwnerPresnatnDe(vo.getPrzwnerPresnatnDe())
                .rcritPblancDe(vo.getRcritPblancDe())
                .searchHouseSecd(vo.getSearchHouseSecd())
                .subscrptAreaCode(vo.getSubscrptAreaCode())
                .subscrptAreaCodeNm(vo.getSubscrptAreaCodeNm())
                .subscrptRceptBgnde(vo.getSubscrptRceptBgnde())
                .subscrptRceptEndde(vo.getSubscrptRceptEndde())
                .totSuplyHshldco(vo.getTotSuplyHshldco())
                .build();
    }

    public ApplyHomePublicPrivateRentVO toVO(){
        return ApplyHomePublicPrivateRentVO.builder()
                .bsnsMbyNm(this.bsnsMbyNm)
                .cntrctCnclsBgnde(this.cntrctCnclsBgnde)
                .cntrctCnclsEndde(this.cntrctCnclsEndde)
                .hmpgAdres(this.hmpgAdres)
                .houseDetailSecd(this.houseDetailSecd)
                .houseDetailSecdNm(this.houseDetailSecdNm)
                .houseManageNo(this.houseManageNo)
                .houseNm(this.houseNm)
                .houseSecd(this.houseSecd)
                .houseSecdNm(this.houseSecdNm)
                .hssplyAdres(this.hssplyAdres)
                .hssplyZip(this.hssplyZip)
                .mdhsTelno(this.mdhsTelno)
                .mvnPrearngeYm(this.mvnPrearngeYm)
                .nsprcNm(this.nsprcNm)
                .pblancNo(this.pblancNo)
                .pblancUrl(this.pblancUrl)
                .przwnerPresnatnDe(this.przwnerPresnatnDe)
                .rcritPblancDe(this.rcritPblancDe)
                .searchHouseSecd(this.searchHouseSecd)
                .subscrptAreaCode(this.subscrptAreaCode)
                .subscrptAreaCodeNm(this.subscrptAreaCodeNm)
                .subscrptRceptBgnde(this.subscrptRceptBgnde)
                .subscrptRceptEndde(this.subscrptRceptEndde)
                .totSuplyHshldco(this.totSuplyHshldco)
                .build();
    }
}
