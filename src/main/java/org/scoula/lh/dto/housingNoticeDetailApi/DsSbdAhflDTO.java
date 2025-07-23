package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.housing.LhHousingAttVO;

// 단지첨부파일 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsSbdAhflDTO {
    @JsonProperty("BZDT_NM")
    private String bzdtNm;

    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    @JsonProperty("SL_PAN_AHFL_DS_CD_NM")
    private String slPanAhflDsCdNm;

    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;

    /**
     * LhHousingAttVO로 변환
     * @param panId 공고 ID
     * @return LhHousingAttVO 객체
     */
    public LhHousingAttVO toLhHousingAttVO(String panId) {
        return LhHousingAttVO.builder()
                .panId(panId)
                .bzdtNm(this.bzdtNm)
                .slPanAhflDsCdNm(this.slPanAhflDsCdNm)
                .cmnAhflNm(this.cmnAhflNm)
                .ahflUrl(this.ahflUrl)
                .build();
    }
}
