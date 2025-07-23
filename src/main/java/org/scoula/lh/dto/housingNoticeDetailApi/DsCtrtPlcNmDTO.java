package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 계약장소명 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsCtrtPlcNmDTO {
    @JsonProperty("SIL_OFC_BCLS_DT")
    private String silOfcBclsDt;

    @JsonProperty("CTRT_PLC_DTL_ADR")
    private String ctrtPlcDtlAdr;

    @JsonProperty("SIL_OFC_GUD_FCTS")
    private String silOfcGudFcts;

    @JsonProperty("CTRT_PLC_ADR")
    private String ctrtPlcAdr;

    @JsonProperty("SIL_OFC_DT")
    private String silOfcDt;

    @JsonProperty("TSK_SCD_CTS")
    private String tskScdCts;

    @JsonProperty("SIL_OFC_TLNO")
    private String silOfcTlno;

    @JsonProperty("SIL_OFC_OPEN_DT")
    private String silOfcOpenDt;
}
