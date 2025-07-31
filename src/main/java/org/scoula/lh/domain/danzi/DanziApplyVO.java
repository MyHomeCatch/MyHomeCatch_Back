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
public class DanziApplyVO {
    private Integer applyId;              // apply ID (PK, AUTO_INCREMENT)
    private Integer complexId;              // 단지 ID (FK)
    private Date sbscAcpStDt;             // 접수기간시작일
    private Date sbscAcpClsgDt;           // 접수기간종료일
    private String rmk;                   // 신청방법(현장접수/인터넷접수)
    private Date pprSbmOpeAncDt;          // 서류제출대상자발표일
    private Date pprAcpStDt;              // 서류접수기간시작일
    private Date pprAcpClsgDt;            // 서류접수기간종료일
    private Date pzwrAncDt;               // 당첨자발표일
    private Date pzwrPprSbmStDt;          // 당첨자서류제출기간시작일
    private Date pzwrPprSbmEdDt;          // 당첨자서류제출기간종료일
    private Date ctrtStDt;                // 계약체결기간시작일
    private Date ctrtEdDt;                // 계약체결기간종료일
}
