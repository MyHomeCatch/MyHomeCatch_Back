package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 기타정보 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsEtcInfoDTO {
    @JsonProperty("ETC_FCTS")
    private String etcFcts;

    @JsonProperty("PAN_DTL_CTS")
    private String panDtlCts;
}
