package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 단지첨부파일명 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSbdAhflNmDTO {
    @JsonProperty("BZDT_NM")
    private String bzdtNm;

    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    @JsonProperty("SL_PAN_AHFL_DS_CD_NM")
    private String slPanAhflDsCdNm;

    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;
}
