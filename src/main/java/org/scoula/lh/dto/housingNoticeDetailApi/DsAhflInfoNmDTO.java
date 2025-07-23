package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.NoticeAttVO;

// 첨부파일정보명 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsAhflInfoNmDTO {
    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    @JsonProperty("SL_PAN_AHFL_DS_CD_NM")
    private String slPanAhflDsCdNm;

    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;

    /**
     * NoticeAttVO로 변환
     * @param panId 공고 ID
     * @return NoticeAttVO 객체
     */
    public NoticeAttVO toVO(String panId) {
        return NoticeAttVO.builder()
                .panId(panId)
                .slPanAhflDsCdNm(this.slPanAhflDsCdNm)
                .cmnAhflNm(this.cmnAhflNm)
                .ahflUrl(this.ahflUrl)
                .build();
    }
}
