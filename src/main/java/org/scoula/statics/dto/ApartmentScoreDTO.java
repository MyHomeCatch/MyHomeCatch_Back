package org.scoula.statics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentScoreDTO {
    private Long aptId;             // 아파트 고유 ID
    private String aptName;         // 아파트 이름
    private String regionCode;      // 지역 코드
    private String address;         // 아파트 주소
    private Integer averageScore;   // 평균 가점 부담 점수 (낮을수록 유리)
    private Integer supplyCount;    // 공급 세대수
}
