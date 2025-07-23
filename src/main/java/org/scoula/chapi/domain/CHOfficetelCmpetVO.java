package org.scoula.chapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelCmpetVO {
    private int cmpetUid;
    private String cmpetRate;
    private String houseManageNo;
    private String houseTy;
    private String modelNo;
    private String pblancNo;
    private int reqCnt;
    private String residntPriorAt;
    private String residntPriorSenm;
    private int suplyHshldco;
}
