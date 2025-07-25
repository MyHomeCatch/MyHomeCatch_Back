package org.scoula.statics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
// 청약 가점 통계 조회 요청
// 예시 : http://localhost:8080/winner-stats/region-score?region=(지역 번호)
// 지역번호 : 100, 200 등
public class ScoreWinnerDTO {
    @JsonProperty("STAT_DE")
    private String statDe; // 통계 기준 일자

    /*
     * 지역 및 거주지 관련 정보
     */
    @JsonProperty("SUBSCRPT_AREA_CODE")
    private String subscribtAreaCode; // 청약 지역 코드

    @JsonProperty("SUBSCRPT_AREA_CODE_NM")
    private String subscribtAreaCodeNm; // 청약 지역 이름 (예: 서울특별시)

    @JsonProperty("RESIDE_SECD")
    private String resideSecd; // 거주자 구분 코드 (예: 1=해당지역, 2=기타지역 등)

    @JsonProperty("RESIDE_SECD_NM")
    private String resideSecdNm; // 거주자 구분 이름 (예: 해당지역, 수도권, 기타지역 등)

    /*
     * 청약 당첨자의 가점 통계 정보
     */
    @JsonProperty("AVRG_SCORE")
    private float avrgScore; // 평균 가점

    @JsonProperty("MED_SCORE")
    private float medScore; // 중위 가점 (가점 분포의 중앙값)

    @JsonProperty("TOP_SCORE")
    private float topScore; // 최고 가점 (가장 높은 가점으로 당첨된 점수)

    @JsonProperty("LWET_SCORE")
    private float lwetScore; // 최저 가점 (가장 낮은 가점으로 당첨된 점수)
}
