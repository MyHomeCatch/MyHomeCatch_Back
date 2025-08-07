package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeAttVO;

// 공고별 첨부파일정보 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsAhflInfoDTO {
    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    @JsonProperty("SL_PAN_AHFL_DS_CD_NM")
    private String slPanAhflDsCdNm;

    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;


    public NoticeAttVO toNoticeAttVO(Integer noticeId) {
        return NoticeAttVO.builder()
                .noticeId(noticeId)
                .slPanAhflDsCdNm(slPanAhflDsCdNm)
                .cmnAhflNm(cmnAhflNm)
                .ahflUrl(ahflUrl)
                .build();
    }
}
