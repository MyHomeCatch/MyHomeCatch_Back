package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.housing.LhHousingVO;
import org.scoula.lh.dto.lhHousing.LhHousingDTO;

// 단지 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSbdDTO {
    @JsonProperty("LCT_ARA_DTL_ADR")
    private String lctAraDtlAdr;

    @JsonProperty("BZDT_NM")
    private String bzdtNm;

    @JsonProperty("EDC_FCL_CTS")
    private String edcFclCts;

    @JsonProperty("MIN_MAX_RSDN_DDO_AR")
    private String minMaxRsdnDdoAr;

    @JsonProperty("SUM_TOT_HSH_CNT")
    private String sumTotHshCnt;

    @JsonProperty("TFFC_FCL_CTS")
    private String tffcFclCts;

    @JsonProperty("LCT_ARA_ADR")
    private String lctAraAdr;

    @JsonProperty("CVN_FCL_CTS")
    private String cvnFclCts;

    @JsonProperty("HTN_FMLA_DS_CD_NM")
    private String htnFmlaDsCdNm;

    @JsonProperty("IDT_FCL_CTS")
    private String idtFclCts;

    @JsonProperty("SPL_INF_GUD_FCTS")
    private String splInfGudFcts;

    @JsonProperty("MVIN_XPC_YM")
    private String mvinXpcYm;

    public LhHousingVO toLhHousingVO(String panId) {
        return LhHousingVO.builder()
                .panId(panId)
                .bzdtNm(bzdtNm)
                .lctAraAdr(lctAraAdr)
                .lctAraDtlAdr(lctAraDtlAdr)
                .minMaxRsdnDdoAr(minMaxRsdnDdoAr)
                .sumTotHshCnt(sumTotHshCnt)
                .htnFmlaDsCdNm(htnFmlaDsCdNm)
                .mvinXpcYm(mvinXpcYm)
                .build();
    }

    public LhHousingDTO toLhHousingDTO(String panId) {
        return LhHousingDTO.builder()
                .panId(panId)
                .bzdtNm(bzdtNm)
                .lctAraAdr(lctAraAdr)
                .lctAraDtlAdr(lctAraDtlAdr)
                .minMaxRsdnDdoAr(minMaxRsdnDdoAr)
                .sumTotHshCnt(sumTotHshCnt)
                .htnFmlaDsCdNm(htnFmlaDsCdNm)
                .mvinXpcYm(mvinXpcYm)
                .build();
    }
}
