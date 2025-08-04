package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseCardVO {
    private String danziId;
    private String bzdtNm;
    private String lctAraAdr;
    private Integer sumTotHshCnt;
    private String minMaxRsdnDdoAr;
    private String aisTpCdNm;
    private String panSs;
    private String cnpCdNm;
    private Date panNtStDt;
    private String ahflUrl;
}
