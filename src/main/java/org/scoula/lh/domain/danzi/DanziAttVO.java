package org.scoula.lh.domain.danzi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanziAttVO {

    private Integer attId;                // att ID (PK, AUTO_INCREMENT)
    private Integer danziId;              // 단지 ID (FK)
    private String flDsCdNm;              // 파일구분명
    private String cmnAhflNm;             // 첨부파일명
    private String ahflUrl;               // 첨부파일 URL

}