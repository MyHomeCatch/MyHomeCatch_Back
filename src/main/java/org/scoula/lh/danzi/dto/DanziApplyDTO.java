package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.DanziApplyVO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanziApplyDTO {
    private Integer applyId;
    private String hsSbscAcpTrgCdNm;
    private Date sbscAcpStDt;
    private Date sbscAcpClsgDt;
    private String rmk;
    private Date pprSbmOpeAncDt;
    private Date pprAcpStDt;
    private Date pprAcpClsgDt;
    private Date pzwrAncDt;
    private Date pzwrPprSbmStDt;
    private Date pzwrPprSbmEdDt;
    private Date ctrtStDt;
    private Date ctrtEdDt;

    public static DanziApplyDTO from(DanziApplyVO vo) {
        return DanziApplyDTO.builder()
                .applyId(vo.getApplyId())
                .hsSbscAcpTrgCdNm(vo.getHsSbscAcpTrgCdNm())
                .sbscAcpStDt(vo.getSbscAcpStDt())
                .sbscAcpClsgDt(vo.getSbscAcpClsgDt())
                .rmk(vo.getRmk())
                .pprSbmOpeAncDt(vo.getPprSbmOpeAncDt())
                .pprAcpStDt(vo.getPprAcpStDt())
                .pprAcpClsgDt(vo.getPprAcpClsgDt())
                .pzwrAncDt(vo.getPzwrAncDt())
                .pzwrPprSbmStDt(vo.getPzwrPprSbmStDt())
                .pzwrPprSbmEdDt(vo.getPzwrPprSbmEdDt())
                .ctrtStDt(vo.getCtrtStDt())
                .ctrtEdDt(vo.getCtrtEdDt())
                .build();
    }
}
