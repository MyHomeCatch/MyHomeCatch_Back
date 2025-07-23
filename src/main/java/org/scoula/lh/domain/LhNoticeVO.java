package org.scoula.lh.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhNoticeVO {
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
}
