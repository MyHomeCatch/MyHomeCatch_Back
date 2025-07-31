package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.DanziAttVO;

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
    

    public DanziAttVO toDanziAttVO(Integer danziId) {
        return DanziAttVO.builder()
                .danziId(danziId)
                .flDsCdNm(slPanAhflDsCdNm)
                .cmnAhflNm(cmnAhflNm)
                .ahflUrl(ahflUrl)
                .build();

    }
}

//        private Integer attId;                // att ID (PK, AUTO_INCREMENT)
//        private Integer danziId;              // 단지 ID (FK)
//        private String flDsCdNm;              // 파일구분명
//        private String cmnAhflNm;             // 첨부파일명
//        private String ahflUrl;               // 첨부파일 URL
