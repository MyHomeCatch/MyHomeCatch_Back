package org.scoula.applyHome.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyHomeSpecialVO {
    private int etcHshldco;
    private String houseManageNo;
    private String houseTy;
    private int insttRecomendHshldco;
    private int lfeFrstHshldco;
    private String lttotTopAmount;
    private int mnychHshldco;
    private String modelNo;
    private int nwbbHshldco;
    private int nwwdsHshldco;
    private int oldParntsSuportHshldco;
    private String pblancNo;
    private int spsplyHshldco;
    private String suplyAr;
    private int suplyHshldco;
    private int transrInsttEnfsnHshldco;
    private int ygmnHshldco;
}