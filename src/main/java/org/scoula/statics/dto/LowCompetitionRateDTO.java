package org.scoula.statics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.statics.domain.CompetitionRateVO;
import org.scoula.statics.domain.HousingInfoVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowCompetitionRateDTO {
    private String PBLANC_NO; // 공고번호
    private String HOUSE_NM; // 주택명(아파트명)
    private String CMPET_RATE; // 경쟁률
    private int SUPLY_HSHLDCO; // 공급세대수
    private int REQ_CNT; // 접수건수
    private String HOUSE_TY; // 주택형(평형)
    private Date RCEPT_ENDDE; // 종료일(오피스텔 테이블 = SUBSCRPT_RCEPT_ENDDE)
    private String SUBSCRPT_AREA_CODE_NM; // 공급지역명

    // VO DTO변환
    public static LowCompetitionRateDTO of(CompetitionRateVO cmpetVo, HousingInfoVO housingVo) {
        return (cmpetVo == null || housingVo == null) ? null : LowCompetitionRateDTO.builder()
                .PBLANC_NO(housingVo.getPBLANC_NO())
                .HOUSE_NM(housingVo.getHOUSE_NM())
                .CMPET_RATE(cmpetVo.getCMPET_RATE())
                .SUPLY_HSHLDCO(cmpetVo.getSUPLY_HSHLDCO())
                .REQ_CNT(cmpetVo.getREQ_CNT())
                .HOUSE_TY(cmpetVo.getHOUSE_TY())
                .RCEPT_ENDDE(housingVo.getRCEPT_ENDDE())
                .SUBSCRPT_AREA_CODE_NM(housingVo.getSUBSCRPT_AREA_CODE_NM())
                .build();
    }
}
