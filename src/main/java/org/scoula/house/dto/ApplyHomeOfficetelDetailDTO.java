package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.dto.CHOfficetelCmpetDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplyHomeOfficetelDetailDTO {
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

    private List<CHOfficetelCmpetDTO> cmpetList;
}
