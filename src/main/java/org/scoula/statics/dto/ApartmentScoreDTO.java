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
// 가점 하위 TOP5 조회 요청
// 예시 : http://localhost:8080/winner-stats/low-score?region=(지역번호)
// 지역번호 01, 02 등
public class ApartmentScoreDTO {
    // 공고관리번호
    @JsonProperty("HOUSE_MANAGE_NO")
    private String houseManageNo;

    // 공고번호
    @JsonProperty("PBLANC_NO")
    private String publicNoticeNo;

    // 주택형 번호
    @JsonProperty("MODEL_NO")
    private String modelNo;

    // 주택형
    @JsonProperty("HOUSE_TY")
    private String houseType;

    // 거주지구분코드
    @JsonProperty("RESIDE_SECD")
    private String resideSecd;

    // 거주지구분명
    @JsonProperty("RESIDE_SENM")
    private String resideSenm;

    // 최저 당첨 점수
    @JsonProperty("LWET_SCORE")
    private String lowestScore;

    // 최고 당첨 점수
    @JsonProperty("TOP_SCORE")
    private String topScore;

    // 평균 당첨 점수
    @JsonProperty("AVRG_SCORE")
    private String averageScore;
}
