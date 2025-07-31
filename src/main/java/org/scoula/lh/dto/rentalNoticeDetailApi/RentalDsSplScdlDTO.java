package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.util.DateParser;
import org.scoula.lh.domain.danzi.DanziApplyVO;

/**
 * 임대주택 공급일정 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class RentalDsSplScdlDTO {

    /**
     * 서류제출대상자발표일
     * 예시: "2025.07.11"
     */
    @JsonProperty("PPR_SBM_OPE_ANC_DT")
    private String pprSbmOpeAncDt;

    /**
     * 서류접수기간종료일
     * 예시: "2025.07.16"
     */
    @JsonProperty("PPR_ACP_CLSG_DT")
    private String pprAcpClsgDt;

    /**
     * 당첨자발표일
     * 예시: "2025.09.22"
     */
    @JsonProperty("PZWR_ANC_DT")
    private String pzwrAncDt;

    /**
     * 계약기간종료일
     * 예시: "2025.10.15"
     */
    @JsonProperty("CTRT_ED_DT")
    private String ctrtEdDt;

    /**
     * 단지명
     * 예시: "고흥남계 국민임대"
     */
    @JsonProperty("SBD_LGO_NM")
    private String sbdLgoNm;

    /**
     * 접수기간시작일
     * 예시: "2025.07.07"
     */
    @JsonProperty("SBSC_ACP_ST_DT")
    private String sbscAcpStDt;

    /**
     * 접수기간종료일
     * 예시: "2025.07.09"
     */
    @JsonProperty("SBSC_ACP_CLSG_DT")
    private String sbscAcpClsgDt;

    /**
     * 계약기간시작일
     * 예시: "2025.10.13"
     */
    @JsonProperty("CTRT_ST_DT")
    private String ctrtStDt;

    /**
     * 서류접수기간시작일
     * 예시: "2025.07.11"
     */
    @JsonProperty("PPR_ACP_ST_DT")
    private String pprAcpStDt;

    public DanziApplyVO toDanziApplyVO(Integer danziId) {
        return DanziApplyVO.builder()
                .danziId(danziId)
                .sbscAcpStDt(DateParser.parseDate(sbscAcpStDt))
                .sbscAcpClsgDt(DateParser.parseDate(sbscAcpStDt))
                .rmk(null)
                .pprSbmOpeAncDt(DateParser.parseDate(pprSbmOpeAncDt))
                .pprAcpStDt(DateParser.parseDate(pprAcpStDt))
                .pprAcpClsgDt(DateParser.parseDate(pprAcpClsgDt))
                .pzwrAncDt(DateParser.parseDate(pzwrAncDt))
                .pzwrPprSbmStDt(null)
                .pzwrPprSbmEdDt(null)
                .ctrtStDt(DateParser.parseDate(ctrtStDt))
                .ctrtEdDt(DateParser.parseDate(ctrtEdDt))
                .build();
    }
}