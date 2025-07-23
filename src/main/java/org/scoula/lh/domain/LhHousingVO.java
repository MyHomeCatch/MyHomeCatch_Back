package org.scoula.lh.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * LH 주택 단지 정보 VO
 * LH_housing 테이블과 매핑
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingVO {

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
}