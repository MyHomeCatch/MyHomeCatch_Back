package org.scoula.statics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HousingInfoVO {
    private int table_code; // 테이블 구분(1= 아파트, 2= 오피스텔)
    private String PBLANC_NO; // 공고번호
    private String HOUSE_NM; // 주택명(아파트명)
    private Date RCEPT_ENDDE; // 종료일(오피스텔 테이블 = SUBSCRPT_RCEPT_ENDDE)
    private String SUBSCRPT_AREA_CODE_NM; // 공급지역명
}
