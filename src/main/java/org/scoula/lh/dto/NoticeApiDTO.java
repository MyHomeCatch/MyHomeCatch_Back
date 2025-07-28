package org.scoula.lh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.util.DateParser;
import org.scoula.lh.domain.LhNoticeVO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeApiDTO {
    @JsonProperty("PAN_NT_ST_DT")
    private String panNtStDt;

    @JsonProperty("PAN_ID")
    private String panId;

    @JsonProperty("AIS_TP_CD_NM")
    private String aisTpCdNm;

    @JsonProperty("CNP_CD_NM")
    private String cnpCdNm;

    @JsonProperty("ALL_CNT")
    private String allCnt;

    @JsonProperty("SPL_INF_TP_CD")
    private String splInfTpCd;

    @JsonProperty("AIS_TP_CD")
    private String aisTpCd;

    @JsonProperty("PAN_DT")
    private String panDt;

    @JsonProperty("RNUM")
    private String rnum;

    @JsonProperty("CCR_CNNT_SYS_DS_CD")
    private String ccrCnntSysDsCd;

    @JsonProperty("DTL_URL")
    private String dtlUrl;

    @JsonProperty("CLSG_DT")
    private String clsgDt;

    @JsonProperty("UPP_AIS_TP_CD")
    private String uppAisTpCd;

    @JsonProperty("PAN_NM")
    private String panNm;

    @JsonProperty("UPP_AIS_TP_NM")
    private String uppAisTpNm;

    @JsonProperty("PAN_SS")
    private String panSs;

    @JsonProperty("DTL_URL_MOB")
    private String dtlUrlMob;

    public LhNoticeVO toVO() {
        return LhNoticeVO.builder()
                .panId(this.panId)
                .uppAisTpCd(this.uppAisTpCd)
                .aisTpCdNm(this.aisTpCdNm)
                .panNm(this.panNm)
                .cnpCdNm(this.cnpCdNm)
                .panSs(this.panSs)
                .allCnt(this.allCnt)
                .dtlUrl(this.dtlUrl)
                .splInfTpCd(this.splInfTpCd)
                .ccrCnntSysDsCd(this.ccrCnntSysDsCd)
                .aisTpCd(this.aisTpCd)
                .panNtStDt(DateParser.parseDate(this.panNtStDt))
                .build();
    }
}
