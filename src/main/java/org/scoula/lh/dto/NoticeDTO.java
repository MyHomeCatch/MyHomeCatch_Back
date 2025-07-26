package org.scoula.lh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhNoticeVO;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    private String panId;
    private String uppAisTpCd;
    private String aisTpCdNm;
    private String panNm;
    private String cnpCdNm;
    private String panSs;
    private String allCnt;
    private String dtlUrl;
    private String splInfTpCd;
    private String ccrCnntSysDsCd;
    private String aisTpCd;

    public static NoticeDTO of(LhNoticeVO vo) {
        return NoticeDTO.builder()
                .panId(vo.getPanId())
                .uppAisTpCd(vo.getUppAisTpCd())
                .aisTpCd(vo.getAisTpCd())
                .panNm(vo.getPanNm())
                .cnpCdNm(vo.getCnpCdNm())
                .panSs(vo.getPanSs())
                .allCnt(vo.getAllCnt())
                .dtlUrl(vo.getDtlUrl())
                .splInfTpCd(vo.getSplInfTpCd())
                .ccrCnntSysDsCd(vo.getCcrCnntSysDsCd())
                .aisTpCd(vo.getAisTpCd())
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
