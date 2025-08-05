package org.scoula.house.domain;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyHomeAptPublicVO {
    private String pblancNo;
    private String bsnsMbyNm;
    private String cnstrctEntrpsNm;
    private Date cntrctCnclsBgnde;
    private Date cntrctCnclsEndde;
    private Date gnrlRnk1CrspareaEndde;
    private Date gnrlRnk1CrspareaRcptde;
    private Date gnrlRnk1EtcAreaEndde;
    private Date gnrlRnk1EtcAreaRcptde;
    private Date gnrlRnk1EtcGgEndde;
    private Date gnrlRnk1EtcGgRcptde;
    private Date gnrlRnk2CrspareaEndde;
    private Date gnrlRnk2CrspareaRcptde;
    private Date gnrlRnk2EtcAreaEndde;
    private Date gnrlRnk2EtcAreaRcptde;
    private Date gnrlRnk2EtcGgEndde;
    private Date gnrlRnk2EtcGgRcptde;
    private String hmpgAdres;
    private String houseManageNo;
    private String houseNm;
    private String houseSecd;
    private String houseSecdNm;
    private String hssplyAdres;
    private String hssplyZip;
    private String mDatTrgetAreaSecd;
    private String mdhsTelno;
    private String mvnPrearngeYm;
    private String nplnPrvorpPublicHouseAt;
    private Date przwnerPresnatnDe;
    private String publicHouseEarthAt;
    private String publicHouseSpclwApplcAt;
    private Date rcritPblancDe;
    private String rentSecdNm;
    private String specltRdnEarthAt;
    private Date spsplyRceptBgnde;
    private Date spsplyRceptEgnde;
    private String subscrptAreaCode;
    private String subscrptAreaCodeNm;
    private Integer totSuplyHshldco;
}