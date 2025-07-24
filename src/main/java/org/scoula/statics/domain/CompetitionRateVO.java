package org.scoula.statics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionRateVO {
    private String PBLANC_NO; // 공고번호
    private String CMPET_RATE; // 경쟁률
    private int SUPLY_HSHLDCO; // 공급세대수
    private int REQ_CNT; // 접수건수
    private String HOUSE_TY; // 주택형(평형)
    // 오피스텔 경쟁률 테이블에 없는 데이터
    private int SUBSCRPT_RANK_CODE; // 순위
    private String RESIDE_SENM; // 거주지역(해당, 기타)
}
