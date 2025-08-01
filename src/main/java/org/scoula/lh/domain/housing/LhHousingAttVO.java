package org.scoula.lh.domain.housing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LH 주택 단지 첨부파일 VO
 * LH_housing_att 테이블과 매핑
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingAttVO {

    private Integer danziId;
    /**
     * 주택첨부파일 ID (Primary Key, Auto Increment)
     */
    private Integer lhHousingAttId;

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
     * 파일구분명
     * 예시: "지역위치도", "단지조감도", "단지배치도", "토지이용계획도", "동호배치도" 등
     */
    private String slPanAhflDsCdNm;

    /**
     * 첨부파일명
     * 예시: "고양장항S-1블록_지역조감도_고.jpg", "고양장항S-1블록_단지배치도_고.jpg" 등
     */
    private String cmnAhflNm;

    /**
     * URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhImageView2.do?fileid=62568304"
     */
    private String ahflUrl;
}
