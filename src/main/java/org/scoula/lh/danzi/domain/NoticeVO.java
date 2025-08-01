package org.scoula.lh.danzi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeVO {
    private Integer noticeId;             // notice ID (PK, AUTO_INCREMENT)
    private String panId;                 // 공고 ID
    private String uppAisTpCd;            // 공고유형명
    private String aisTpCdNm;             // 공고 세부 유형명
    private String panNm;                 // 공고명
    private String cnpCdNm;               // 지역명
    private String panSs;                 // 공고상태
    private String allCnt;                // 전체조회건수
    private Date panNtStDt;               // 공고 발행 날짜
    private String dtlUrl;                // 공고상세URL
    private String splInfTpCd;            // 광급정보구분코드
    private String ccrCnntSysDsCd;        // 시스템구분코드
    private String aisTpCd;               // 유형코드
}