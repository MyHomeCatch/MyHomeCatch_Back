package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.util.DateParser;
import org.scoula.house.util.DateRangeParser;
import org.scoula.lh.domain.danzi.DanziApplyVO;

import java.util.Date;

// 공급일정 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSplScdlDTO {
    @JsonProperty("RMK")
    private String rmk;                      // 비고/신청방법 (예: 현장접수/인터넷접수)

    @JsonProperty("PZWR_ANC_DT")
    private String pzwrAncDt;                // 당첨자 발표일

    @JsonProperty("CTRT_ED_DT")
    private String ctrtEdDt;                 // 계약 종료일 (계약체결기간 종료일)

    @JsonProperty("HS_SBSC_ACP_TRG_CD_NM")
    private String hsSbscAcpTrgCdNm;         // 주택청약 접수대상 코드명

    @JsonProperty("ACP_DTTM")
    private String acpDttm;                  // 접수일시

    @JsonProperty("PZWR_PPR_SBM_ST_DT")
    private String pzwrPprSbmStDt;           // 당첨자 서류제출 시작일

    @JsonProperty("SPL_SCD_GUD_FCTS")
    private String splScdGudFcts;            // 공급일정 안내사항

    @JsonProperty("CTRT_ST_DT")
    private String ctrtStDt;                 // 계약 시작일 (계약체결기간 시작일)

    @JsonProperty("PZWR_PPR_SBM_ED_DT")
    private String pzwrPprSbmEdDt;           // 당첨자 서류제출 종료일


    public DanziApplyVO toDanziApplyVO(Integer danziId) {
        return DanziApplyVO.builder()
                .danziId(danziId)
                .sbscAcpStDt(DateRangeParser.parseStartDate(acpDttm))
                .sbscAcpClsgDt(DateRangeParser.parseEndDate(acpDttm))
                .rmk(rmk)
                .pprSbmOpeAncDt(null)
                .pprAcpStDt(null)
                .pprAcpClsgDt(null)
                .pzwrAncDt(DateParser.parseDate(pzwrAncDt))
                .pzwrPprSbmStDt(DateParser.parseDate(pzwrPprSbmStDt))
                .pzwrPprSbmEdDt(DateParser.parseDate(pzwrPprSbmEdDt))
                .ctrtStDt(DateParser.parseDate(ctrtStDt))
                .ctrtEdDt(DateParser.parseDate(ctrtEdDt))
                .build();
    }
}
