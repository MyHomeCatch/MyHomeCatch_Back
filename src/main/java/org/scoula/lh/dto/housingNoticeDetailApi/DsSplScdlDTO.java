package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhHousingApplyVO;

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
                .pzwrAncDt(parseDate(this.pzwrAncDt))
                .pzwrPprSbmStDt(parseDate(this.pzwrPprSbmStDt))
                .pzwrPprSbmEdDt(parseDate(this.pzwrPprSbmEdDt))
                .ctrtStDt(parseDate(this.ctrtStDt))
                .ctrtEdDt(parseDate(this.ctrtEdDt))
                .build();
    }

    /**
     * 문자열 날짜를 Date 객체로 변환
     * @param dateString "20200101" 형태의 날짜 문자열
     * @return Date 객체 (파싱 실패시 null)
     */
    private Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            // "20200101" 형태를 Date 객체로 변환
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            return formatter.parse(dateString.trim());
        } catch (ParseException e) {
            // 파싱 실패시 로그 출력 후 null 반환
            System.err.println("날짜 파싱 실패: " + dateString + " - " + e.getMessage());
            return null;
        }
    }
}
