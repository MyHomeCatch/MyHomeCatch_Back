package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.dto.CHOfficetelCmpetDTO;
import org.scoula.chapi.dto.CHOfficetelDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplyHomeOfficetelDetailDTO {
    // 오피스텔공고 주택형별 정보
    private int modelUid;

    private String excluseAr;

    private String gp;

    private String houseManageNo;

    private String modelNo;

    private String pblancNo;

    private int subscrptReqstAmount;

    private int suplyAmount;

    private int suplyHshldco;

    private String tp;

    // 오피스텔공고 경쟁률 정보
    private List<CHOfficetelCmpetDTO> cmpetList;
}
