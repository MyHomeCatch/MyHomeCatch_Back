package org.scoula.lh.domain.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LhRentalAttVO {
    private Integer lhRentalAttId;    // 임대주택첨부파일ID
    private String panId;             // 공고ID
    private String locNtNm;           // 단지명
    private String lsSplInfUplFlDsCdNm; // 파일구분명
    private String cmnAhflNm;         // 첨부파일명
    private String ahflUrl;           // url
}
