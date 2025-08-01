package org.scoula.lh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeVO;
import org.scoula.lh.domain.LhNoticeVO;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    private int noticeId;
    private String panId;
    private String uppAisTpCd;
    private String supplyCategory;
    private String panName;
    private String cnpCdNm;
    private String panState;
    private String allCnt;
    private String noticeUrl;
    private String splInfTpCd;
    private String ccrCnntSysDsCd;
    private String aisTpCd;
    private Date noticeDate;

    public static NoticeDTO of(LhNoticeVO vo) {
        return NoticeDTO.builder()
                .panId(vo.getPanId())
                .uppAisTpCd(vo.getUppAisTpCd())
                .aisTpCd(vo.getAisTpCd())
                .panName(vo.getPanNm())
                .cnpCdNm(vo.getCnpCdNm())
                .panState(vo.getPanSs())
                .allCnt(vo.getAllCnt())
                .noticeUrl(vo.getDtlUrl())
                .splInfTpCd(vo.getSplInfTpCd())
                .ccrCnntSysDsCd(vo.getCcrCnntSysDsCd())
                .aisTpCd(vo.getAisTpCd())
                .noticeDate(vo.getPanNtStDt())
                .build();
    }

    public static NoticeDTO of(NoticeVO vo) {
        return NoticeDTO.builder()
                .noticeId(vo.getNoticeId())
                .panId(vo.getPanId())
                .uppAisTpCd(vo.getUppAisTpCd())
                .aisTpCd(vo.getAisTpCd())
                .panName(vo.getPanNm())
                .cnpCdNm(vo.getCnpCdNm())
                .panState(vo.getPanSs())
                .allCnt(vo.getAllCnt())
                .noticeUrl(vo.getDtlUrl())
                .splInfTpCd(vo.getSplInfTpCd())
                .ccrCnntSysDsCd(vo.getCcrCnntSysDsCd())
                .aisTpCd(vo.getAisTpCd())
                .noticeDate(vo.getPanNtStDt())
                .build();
    }

    public QueryParamDTO toQueryParamDTO() {
        return QueryParamDTO.builder()
                .panId(this.panId)
                .splInfTpCd(this.splInfTpCd)
                .ccrCnntSysDsCd(this.ccrCnntSysDsCd)
                .uppAisTpCd(this.uppAisTpCd)
                .aisTpCd(this.aisTpCd)
                .build();
    }
}
