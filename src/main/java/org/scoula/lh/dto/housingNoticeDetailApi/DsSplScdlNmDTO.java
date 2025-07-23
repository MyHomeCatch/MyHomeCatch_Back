package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 공급일정명 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSplScdlNmDTO {
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
}
