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
public class QueryParamDTO {
    private String panId;
    private String splInfTpCd;
    private String ccrCnntSysDsCd;
    private String uppAisTpCd;
    private String aisTpCd;

    public static QueryParamDTO of(LhNoticeVO vo) {
        return QueryParamDTO.builder()
                .panId(vo.getPanId())
                .splInfTpCd(vo.getSplInfTpCd())
                .ccrCnntSysDsCd(vo.getCcrCnntSysDsCd())
                .uppAisTpCd(vo.getUppAisTpCd())
                .aisTpCd(vo.getAisTpCd())
                .build();
    }
}
