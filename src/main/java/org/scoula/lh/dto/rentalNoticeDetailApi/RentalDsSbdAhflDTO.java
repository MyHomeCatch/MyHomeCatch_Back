package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.rental.LhRentalAttVO;

/**
 * 임대주택 단지 첨부파일 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDsSbdAhflDTO {

    /**
     * 단지명
     * 예시: "(세종)행정중심복합도시 6-3M중1블록 행복주택"
     */
    @JsonProperty("LCC_NT_NM")
    private String lccNtNm;

    /**
     * 첨부파일 URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhImageView2.do?fileid=58507079"
     */
    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    /**
     * 파일구분명
     * 예시: "단지조감도", "단지배치도", "동호배치도", "위치도", "카다로그" 등
     */
    @JsonProperty("LS_SPL_INF_UPL_FL_DS_CD_NM")
    private String lsSplInfUplFlDsCdNm;

    /**
     * 첨부파일명
     * 예시: "6-3M중1_단지조감도.png", "6-3M중1_단지배치도.png" 등
     */
    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;

    /**
     * API DTO를 VO로 변환
     * @param panId 공고ID (외부에서 주입)
     * @return LHRentalAttVO
     */
    public LhRentalAttVO toLhRentalAttVO(String panId) {
        return LhRentalAttVO.builder()
                .panId(panId)
                .locNtNm(this.lccNtNm)
                .lsSplInfUplFlDsCdNm(this.lsSplInfUplFlDsCdNm)
                .cmnAhflNm(this.cmnAhflNm)
                .ahflUrl(this.ahflUrl)
                .build();
    }
}