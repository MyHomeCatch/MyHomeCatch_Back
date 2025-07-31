package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.util.DateParser;
import org.scoula.lh.domain.danzi.DanziVO;
import org.scoula.lh.domain.rental.LhRentalVO;

/**
 * 임대주택 단지 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDsSbdDTO {

    /**
     * 단지주소
     * 예시: "세종특별자치시 산울동"
     */
    @JsonProperty("LGDN_ADR")
    private String lgdnAdr;

    /**
     * 단지상세주소
     * 예시: "6-3M1"
     */
    @JsonProperty("LGDN_DTL_ADR")
    private String lgdnDtlAdr;

    /**
     * 총세대수
     * 예시: "238"
     */
    @JsonProperty("HSH_CNT")
    private String hshCnt;

    /**
     * 난방방식
     * 예시: "지역난방", "개별난방"
     */
    @JsonProperty("HTN_FMLA_DESC")
    private String htnFmlaDesc;

    /**
     * 단지명
     * 예시: "(세종)행정중심복합도시 6-3M중1블록 행복주택"
     */
    @JsonProperty("LCC_NT_NM")
    private String lccNtNm;

    /**
     * 전용면적
     * 예시: "21.59~36.9"
     */
    @JsonProperty("DDO_AR")
    private String ddoAr;

    /**
     * 안내사항
     */
    @JsonProperty("SPL_INF_GUD_FCTS")
    private String splInfGudFcts;

    /**
     * 입주예정월
     * 예시: "2025.11"
     */
    @JsonProperty("MVIN_XPC_YM")
    private String mvinXpcYm;

    public LhRentalVO toLhRentalVO(String panId) {
        return LhRentalVO.builder()
                .panId(panId)
                .lccNtNm(lccNtNm)
                .lgdnAdr(lgdnAdr)
                .lgdnDtlAdr(lgdnDtlAdr)
                .ddoAr(ddoAr)
                .hshCnt(hshCnt)
                .htnFmlaDesc(htnFmlaDesc)
                .mvinXpcYm(mvinXpcYm)
                .build();
    }

    public DanziVO toDanziVO() {
        return DanziVO.builder()
                .bzdtNm(lccNtNm)
                .lctAraAdr(lgdnAdr)
                .lctAraDtlAdr(lgdnDtlAdr)
                .minMaxRsdnDdoAr(ddoAr)
                .sumTotHshCnt(Integer.parseInt(hshCnt))
                .htnFmlaDeCoNm(htnFmlaDesc)
                .mvinXpcYm(DateParser.parseDate(mvinXpcYm))
                .build();
    }
}