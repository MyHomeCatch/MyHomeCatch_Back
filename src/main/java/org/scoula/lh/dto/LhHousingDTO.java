package org.scoula.lh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhHousingVO;

import java.util.Date;

/**
 * LH 주택 단지 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingDTO {

    /**
     * 주택정보 ID (Primary Key, Auto Increment)
     */
    private Integer lhHousingId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 단지명
     * 예시: "고양장항 S-1", "서울강남권도시형주택(석촌)" 등
     */
    private String bzdtNm;

    /**
     * 단지주소
     * 예시: "경기도 고양시 일산동구 장항동"
     */
    private String lctAraAdr;

    /**
     * 단지상세주소
     * 예시: "고양장항 S-1블록 공공분양주택"
     */
    private String lctAraDtlAdr;

    /**
     * 전용면적
     * 예시: "59.92 ~ 84.97", "17.53~26.72"
     */
    private String minMaxRsdnDdoAr;

    /**
     * 총세대수
     * 예시: "869", "22"
     */
    private String sumTotHshCnt;

    /**
     * 난방방식
     * 예시: "지역난방", "개별난방"
     */
    private String htnFmlaDsCdNm;

    /**
     * 입주예정월
     * 예시: 2028-03-01 (2028년 03월)
     */
    private String mvinXpcYm;

    /**
     * LhHousingVO를 LhHousingDTO로 변환
     * @param vo LhHousingVO 객체
     * @return LhHousingDTO 객체
     */
    public static LhHousingDTO of(LhHousingVO vo) {
        return LhHousingDTO.builder()
                .lhHousingId(vo.getLhHousingId())
                .panId(vo.getPanId())
                .bzdtNm(vo.getBzdtNm())
                .lctAraAdr(vo.getLctAraAdr())
                .lctAraDtlAdr(vo.getLctAraDtlAdr())
                .minMaxRsdnDdoAr(vo.getMinMaxRsdnDdoAr())
                .sumTotHshCnt(vo.getSumTotHshCnt())
                .htnFmlaDsCdNm(vo.getHtnFmlaDsCdNm())
                .mvinXpcYm(vo.getMvinXpcYm())
                .build();
    }

    /**
     * LhHousingDTO를 LhHousingVO로 변환
     * @return LhHousingVO 객체
     */
    public LhHousingVO toVO() {
        return LhHousingVO.builder()
                .lhHousingId(this.lhHousingId)
                .panId(this.panId)
                .bzdtNm(this.bzdtNm)
                .lctAraAdr(this.lctAraAdr)
                .lctAraDtlAdr(this.lctAraDtlAdr)
                .minMaxRsdnDdoAr(this.minMaxRsdnDdoAr)
                .sumTotHshCnt(this.sumTotHshCnt)
                .htnFmlaDsCdNm(this.htnFmlaDsCdNm)
                .mvinXpcYm(this.mvinXpcYm)
                .build();
    }
}