package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalApplyVO {
    long lh_rental_id; //'임대주택신청정보ID',
    String pan_id; //'공고ID',
    String sbd_lgo_nm; // '단지명',
    String sbsc_acp_st_dt; // '접수기간시작일',
    String sbsc_acp_clsg_dt; // '접수기간종료일',
    String ppr_sbm_ope_anc_dt; // '서류제출대상자발표일',
    String ppr_acp_st_dt; // DATE COMMENT '서류접수기간시작일',
    String ppr_acp_clsg_dt; // DATE COMMENT '서류접수기간종료일',
    String pzwr_anc_dt; // DATE COMMENT '당첨자발표일',
    String ctrt_st_dt; // DATE COMMENT '계약체결기간시작일',
    String ctrt_ed_dt; // DATE COMMENT '계약체결기간종료일',
}
