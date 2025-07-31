package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingApplyVO {
    long lh_housing_apply_id; // 주택신청정보ID
    String pan_id; // 공고ID
    String hs_sbsc_acp_trg_cd_nm; // 구분 (생애최초, 무순위 등)
    String acp_dttm; //'신청일시(2019.03.06 15:00 ~ 2020.03.04 18:00)',
    String start_dttm; // DATETIME COMMENT '신청 시작 일시',
    String end_dttm; //'신청 마감 일시';
    String rmk; //'신청방법',
    String pzwr_anc_dt; //DATE COMMENT '당첨자 발표 일자',
    String pzwr_ppr_sbm_st_dt; // DATE COMMENT '당첨자서류제출기간시작일',
    String pzwr_ppr_sbm_ed_dt; //DATE COMMENT '당첨자서류제출기간종료일',
    String ctrt_st_dt; // DATE COMMENT '계약체결기간시작일',
    String ctrt_ed_dt; // DATE COMMENT '계약체결기간종료일',
}
