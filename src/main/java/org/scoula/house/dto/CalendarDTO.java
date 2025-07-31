package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.LhHousingApplyVO;
import org.scoula.house.domain.LhNoticeVO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {

    String pan_id; // '공고유형명',
    String pan_nm; //'공고명',
    String pan_ss; //'공고상태',
    String upp_ais_tp_cd; //'공고유형명',
    String ais_tp_cd_nm; //'공고 세부 유형명',
    String cnp_cd_nm; //'지역명',
    String applyStartDate; // 접수 시작일

    String all_cnt; // '전체조회건수',
    String dtl_url; // '공고상세URL',

    // vo -> dto 변환
    public static CalendarDTO of (String date, LhNoticeVO vo) {
        return CalendarDTO.builder()
                .pan_id(vo.getPan_id())
                .upp_ais_tp_cd(vo.getUpp_ais_tp_cd())
                .ais_tp_cd_nm(vo.getAis_tp_cd_nm())
                .pan_nm(vo.getPan_nm())
                .cnp_cd_nm(vo.getCnp_cd_nm())
                .pan_ss(vo.getPan_ss())
                .applyStartDate(date)
                .build();
    }
}
