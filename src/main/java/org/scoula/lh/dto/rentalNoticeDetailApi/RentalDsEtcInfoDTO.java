package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 임대주택 기타 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDsEtcInfoDTO {

    /**
     * 기타사항
     * 예시: "금회 모집 예비입주자는 현재 공가 및 향후 해약이 발생할 경우를 대비하여 모집하는 것으로..."
     */
    @JsonProperty("ETC_CTS")
    private String etcCts;

    /**
     * 정정/취소사유
     * 예시: "" (보통 빈 문자열)
     */
    @JsonProperty("CRC_RSN")
    private String crcRsn;
}