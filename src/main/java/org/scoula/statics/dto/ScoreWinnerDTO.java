package org.scoula.statics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreWinnerDTO {
    private String regionCode;      // 지역 코드
    private String regionName;      // 지역명
    private Integer avgScore;       // 평균 당첨 점수
    private Integer medianScore;    // 중간값 점수
    private Integer lowestScore;    // 최저 점수
    private Integer highestScore;   // 최고 점수
    private Integer winnerCount;    // 당첨자 수
    private String statDate;        // 통계 기준일
}
