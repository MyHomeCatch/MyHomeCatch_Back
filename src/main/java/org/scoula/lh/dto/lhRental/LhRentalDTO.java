package org.scoula.lh.dto.lhRental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.rental.LhRentalVO;

/**
 * LH 임대주택 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalDTO {

    /**
     * 임대주택정보 ID (Primary Key, Auto Increment)
     */
    private Integer lhRentalId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 단지명
     * 예시: "(세종)행정중심복합도시 6-3M중1블록 행복주택", "고흥남계 국민임대" 등
     */
    private String lccNtNm;

    /**
     * 단지주소
     * 예시: "세종특별자치시 산울동", "전라남도 고흥군 고흥읍 터미널길 17-35" 등
     */
    private String lgdnAdr;

    /**
     * 단지상세주소
     * 예시: "6-3M1", "[고흥남계]" 등
     */
    private String lgdnDtlAdr;

    /**
     * 전용면적
     * 예시: "21.59~36.9", "27.82~46.7" 등
     */
    private String ddoAr;

    /**
     * 총세대수
     * 예시: "238", "362", "856" 등
     */
    private String hshCnt;

    /**
     * 난방방식
     * 예시: "지역난방", "개별난방" 등
     */
    private String htnFmlaDesc;

    /**
     * 입주예정월
     * 예시: "2025.11", "2020.09", "2019.02" 등
     */
    private String mvinXpcYm;

    /**
     * LhRentalVO를 LhRentalDTO로 변환
     * @param vo LhRentalVO 객체
     * @return LhRentalDTO 객체
     */
    public static LhRentalDTO of(LhRentalVO vo) {
        return LhRentalDTO.builder()
                .lhRentalId(vo.getLhRentalId())
                .panId(vo.getPanId())
                .lccNtNm(vo.getLccNtNm())
                .lgdnAdr(vo.getLgdnAdr())
                .lgdnDtlAdr(vo.getLgdnDtlAdr())
                .ddoAr(vo.getDdoAr())
                .hshCnt(vo.getHshCnt())
                .htnFmlaDesc(vo.getHtnFmlaDesc())
                .mvinXpcYm(vo.getMvinXpcYm())
                .build();
    }

    /**
     * LhRentalDTO를 LhRentalVO로 변환
     * @return LhRentalVO 객체
     */
    public LhRentalVO toVO() {
        return LhRentalVO.builder()
                .lhRentalId(this.lhRentalId)
                .panId(this.panId)
                .lccNtNm(this.lccNtNm)
                .lgdnAdr(this.lgdnAdr)
                .lgdnDtlAdr(this.lgdnDtlAdr)
                .ddoAr(this.ddoAr)
                .hshCnt(this.hshCnt)
                .htnFmlaDesc(this.htnFmlaDesc)
                .mvinXpcYm(this.mvinXpcYm)
                .build();
    }
}