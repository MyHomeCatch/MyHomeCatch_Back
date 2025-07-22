package org.scoula.chapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelModelVO {
    private String excluseAr;

    private String gp;

    private String houseManageNo;

    private String modelNo;

    private String pblancNo;

    private String subscrptReqstAmount;

    private String suplyAmount;

    private int suplyHshldco;

    private String tp;
}
