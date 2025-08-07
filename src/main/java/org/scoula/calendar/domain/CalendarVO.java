package org.scoula.calendar.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarVO {
    private int danzi_id; // 단지 id
    private int notice_id; // 공고 id
    private String cnp_cd_nm; // 공급 지역
    private String bzdt_nm; // 단지명
    private String lct_ara_adr; // 단지 주소
    private String lct_ara_dtl_adr; // 단지 상세 주소
    private String ais_tp_cd; // 공고 유형 코드
    private String ais_tp_cd_nm; // 공고 유형명
    private String hs_sbsc_acp_trg_cd_nm; // 구분(분양주택의 경우만)
    private String sbsc_acp_st_dt; // 신청시작일
    private String sbsc_acp_clsg_dt; // 신청 종료일
}
