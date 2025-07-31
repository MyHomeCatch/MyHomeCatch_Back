package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhNoticeVO {
    String pan_id; // '공고유형명',
    String upp_ais_tp_cd; //'공고유형코드',
    String ais_tp_cd_nm; //'공고 유형명',
    String pan_nm; //'공고명',
    String cnp_cd_nm; //'지역명',
    String pan_ss; //'공고상태',
    String all_cnt; // '전체조회건수',
    String dtl_url; // '공고상세URL',
    String spl_inf_tp_cd;
    String ccr_cnnt_sys_ds_cd;
    String ais_tp_cd;
}
