package org.scoula.lh.domain.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalVO {
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
}