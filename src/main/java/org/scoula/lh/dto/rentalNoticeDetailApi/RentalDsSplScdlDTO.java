package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.domain.rental.LhRentalApplyVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * LhRentalApplyVO로 변환
     * @param panId 공고 ID
     * @return LhRentalApplyVO 객체
     */
    public LhRentalApplyVO toLhRentalApplyVO(String panId) {
        return LhRentalApplyVO.builder()
                .panId(panId)
                .sbdLgoNm(this.sbdLgoNm)
                .sbscAcpStDt(this.sbscAcpStDt)
                .sbscAcpClsgDt(this.sbscAcpClsgDt)
                .pprSbmOpeAncDt(this.pprSbmOpeAncDt)
                .pprAcpStDt(parseDate(this.pprAcpStDt))
                .pprAcpClsgDt(parseDate(this.pprAcpClsgDt))
                .pzwrAncDt(parseDate(this.pzwrAncDt))
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