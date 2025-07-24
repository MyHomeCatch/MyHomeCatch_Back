package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
                .pzwrAncDt(parseDate(this.pzwrAncDt))
                .pzwrPprSbmStDt(parseDate(this.pzwrPprSbmStDt))
                .pzwrPprSbmEdDt(parseDate(this.pzwrPprSbmEdDt))
                .ctrtStDt(parseDate(this.ctrtStDt))
                .ctrtEdDt(parseDate(this.ctrtEdDt))
                .build();
    }

    private Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        String cleanDateString = dateString.trim();

        // 지원할 날짜 형식들
        String[] dateFormats = {
                "yyyyMMdd",        // 20200101
                "yyyy.MM.dd",      // 2025.06.30
                "yyyy-MM-dd",      // 2025-06-30 (추가로 지원)
                "yyyy/MM/dd"       // 2025/06/30 (추가로 지원)
        };

        for (String format : dateFormats) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                formatter.setLenient(false); // 엄격한 파싱 모드
                return formatter.parse(cleanDateString);
            } catch (ParseException e) {
                // 현재 형식으로 파싱 실패, 다음 형식 시도
                continue;
            }
        }

        // 파싱 실패시 로그 출력 후 null 반환
        System.err.println("날짜 파싱 실패: " + dateString);
        return null;
    }
}
