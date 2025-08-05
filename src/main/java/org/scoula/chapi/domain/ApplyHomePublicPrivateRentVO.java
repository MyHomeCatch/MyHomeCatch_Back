package org.scoula.chapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplyHomePublicPrivateRentVO {

    private String bsnsMbyNm;

    private Date cntrctCnclsBgnde;

    private Date cntrctCnclsEndde;

    private String hmpgAdres;

    private String houseDetailSecd;

    private String houseDetailSecdNm;

    private String houseManageNo;

    private String houseNm;

    private String houseSecd;

    private String houseSecdNm;

    private String hssplyAdres;

    private String hssplyZip;

    private String mdhsTelno;

    private String mvnPrearngeYm;

    private String nsprcNm;

    private String pblancNo;

    private String pblancUrl;

    private Date przwnerPresnatnDe;

    private Date rcritPblancDe;

    private String searchHouseSecd;

    private String subscrptAreaCode;

    private String subscrptAreaCodeNm;

    private Date subscrptRceptBgnde;

    private Date subscrptRceptEndde;

    private int totSuplyHshldco;
}
