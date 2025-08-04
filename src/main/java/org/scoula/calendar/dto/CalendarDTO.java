package org.scoula.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.calendar.domain.CalendarVO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarDTO {

    /*
    공고유형코드 ais_tp_cd
    공고유형명 ais_tp_cd_nm
    구분 hs_sbsc_acp_trg_cd_nm
    단지주소 lct_ara_adr + lct_ara_dtl_adr
    단지명 bzdt_nm
   단지ID danzi_id
    신청시작일 sbsc_acp_st_dt
    신청종료일 sbsc_acp_clsg_dt
    공급지역 cnp_cd_nm
     */

    private int danzi_id; // 단지 id
    private String region; // 공급 지역
    private String danziName; // 단지명
    private String address; // 단지 주소
    private String noticeTypeCode; // 공고 유형 코드
    private String noticeTypeName; // 공고 유형명
    private String typeOption; // 구분(분양주택의 경우만)
    private String startDate; // 신청시작일
    private String endDate; // 신청 종료일

    public static CalendarDTO of(CalendarVO vo) {
        return CalendarDTO.builder()
                .danzi_id(vo.getDanzi_id())
                .region(vo.getCnp_cd_nm())
                .danziName(vo.getBzdt_nm())
                .address(vo.getLct_ara_adr() + " " + vo.getLct_ara_dtl_adr())
                .noticeTypeCode(vo.getAis_tp_cd())
                .noticeTypeName(vo.getAis_tp_cd_nm())
                .typeOption(vo.getHs_sbsc_acp_trg_cd_nm())
                .startDate(vo.getSbsc_acp_st_dt())
                .endDate(vo.getSbsc_acp_clsg_dt())
                .build();
    }
}
