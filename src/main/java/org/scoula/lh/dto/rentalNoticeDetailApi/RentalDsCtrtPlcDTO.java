package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 임대주택 계약장소 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDsCtrtPlcDTO {

    /**
     * 접수처 상세주소
     * 예시: "LH세종특별본부 1층 주택판매팀"
     */
    @JsonProperty("CTRT_PLC_DTL_ADR")
    private String ctrtPlcDtlAdr;

    /**
     * 접수처 안내사항
     * 예시: "모집관련 모든 정보는 공고문(첨부파일)을 반드시 확인하시고 신청하시기 바랍니다..."
     */
    @JsonProperty("SIL_OFC_GUD_FCTS")
    private String silOfcGudFcts;

    /**
     * 접수처 주소
     * 예시: "세종특별자치시 가름로 238-3(어진동)"
     */
    @JsonProperty("CTRT_PLC_ADR")
    private String ctrtPlcAdr;

    /**
     * 운영기간 시작일시
     * 예시: "2027.07.07"
     */
    @JsonProperty("TSK_ST_DTTM")
    private String tskStDttm;

    /**
     * 운영기간 종료일시
     * 예시: "2027.07.09"
     */
    @JsonProperty("TSK_ED_DTTM")
    private String tskEdDttm;

    /**
     * 전화번호
     * 예시: "1600-1004"
     */
    @JsonProperty("SIL_OFC_TLNO")
    private String silOfcTlno;
}