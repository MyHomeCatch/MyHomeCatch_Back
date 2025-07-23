package org.scoula.applyHome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyHomeAnalysisVO {
    private Integer cmpetId;              // 경쟁 ID (PK)
    private String pblancNo;              // 공고번호 (FK)
    private String cmpetRate;             // 경쟁률
    private String houseManageNo;         // 주택관리번호
    private String houseTy;               // 주택형
    private String modelNo;               // 모델번호
    private Integer reqCnt;               // 접수건수
    private String resideSecd;            // 거주코드(01/02)
    private String resideSenm;            // 거주지역
    private Integer subscrptRankCode;     // 순위(1/2)
    private Integer suplyHshldco;         // 공급세대수
    private String lwetScore;            // 최저점
    private String topScore;             // 최고점
    private String avrgScore;            // 평균점
}
