package org.scoula.lh.domain.danzi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanziVO {
    private Integer danziId;              // 단지ID (PK, AUTO_INCREMENT)
    private String dzdtNm;                // 단지명
    private String lctAraAdr;             // 단지주소
    private String lctAraDtlAdr;          // 단지상세주소
    private String minMaxRsdnDdoAr;       // 전용면적
    private Integer sumTotHshCnt;         // 총세대수
    private String htnFmlaDeCoNm;         // 난방방식
    private Date mvinXpcYm;               // 입주예정월
}