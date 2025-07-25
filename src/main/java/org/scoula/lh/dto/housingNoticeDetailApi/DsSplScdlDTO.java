package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.util.DateParser;
import org.scoula.lh.domain.housing.LhHousingApplyVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// 공급일정 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSplScdlDTO {
    @JsonProperty("RMK")
    private String rmk;

    @JsonProperty("PZWR_ANC_DT")
    private String pzwrAncDt;

    @JsonProperty("CTRT_ED_DT")
    private String ctrtEdDt;

    @JsonProperty("HS_SBSC_ACP_TRG_CD_NM")
    private String hsSbscAcpTrgCdNm;

    @JsonProperty("ACP_DTTM")
    private String acpDttm;

    @JsonProperty("PZWR_PPR_SBM_ST_DT")
    private String pzwrPprSbmStDt;

    @JsonProperty("SPL_SCD_GUD_FCTS")
    private String splScdGudFcts;

    @JsonProperty("CTRT_ST_DT")
    private String ctrtStDt;

    @JsonProperty("PZWR_PPR_SBM_ED_DT")
    private String pzwrPprSbmEdDt;

    /**
     * LhHousingApplyVO로 변환
     * @param panId 공고 ID
     * @return LhHousingApplyVO 객체
     */
    public LhHousingApplyVO toVO(String panId) {
        return LhHousingApplyVO.builder()
                .panId(panId)
                .hsSbscAcpTrgCdNm(this.hsSbscAcpTrgCdNm)
                .acpDttm(this.acpDttm)
                .rmk(this.rmk)
                .pzwrAncDt(DateParser.parseDate(this.pzwrAncDt))
                .pzwrPprSbmStDt(DateParser.parseDate(this.pzwrPprSbmStDt))
                .pzwrPprSbmEdDt(DateParser.parseDate(this.pzwrPprSbmEdDt))
                .ctrtStDt(DateParser.parseDate(this.ctrtStDt))
                .ctrtEdDt(DateParser.parseDate(this.ctrtEdDt))
                .build();
    }
}
