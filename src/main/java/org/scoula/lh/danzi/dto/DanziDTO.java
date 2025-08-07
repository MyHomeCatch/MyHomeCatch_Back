package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.DanziVO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanziDTO {
    private Integer danziId;
    private String bzdtNm;
    private String lctAraAdr;
    private String lctAraDtlAdr;
    private String minMaxRsdnDdoAr;
    private Integer sumTotHshCnt;
    private String htnFmlaDeCoNm;
    private Date mvinXpcYm;

    public static DanziDTO from(DanziVO vo) {
        return DanziDTO.builder()
                .danziId(vo.getDanziId())
                .bzdtNm(vo.getBzdtNm())
                .lctAraAdr(vo.getLctAraAdr())
                .lctAraDtlAdr(vo.getLctAraDtlAdr())
                .minMaxRsdnDdoAr(vo.getMinMaxRsdnDdoAr())
                .sumTotHshCnt(vo.getSumTotHshCnt())
                .htnFmlaDeCoNm(vo.getHtnFmlaDeCoNm())
                .mvinXpcYm(vo.getMvinXpcYm())
                .build();
    }
}
