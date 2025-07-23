package org.scoula.lh.domain.rental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalApplyVO {

    /**
     * 임대주택신청정보 ID (Primary Key, Auto Increment)
     */
    private Integer lhRentalId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 단지명
     * 예시: "오산세교 A-6BL 국민임대주택"
     */
    private String sbdLgoNm;

    /**
     * 접수기간시작일
     * 예시: "2025.07.07"
     */
    private String sbscAcpStDt;

    /**
     * 접수기간종료일
     * 예시: "2025.07.09"
     */
    private String sbscAcpClsgDt;

    /**
     * 서류제출대상자발표일
     * 예시: "2025.07.11"
     */
    private String pprSbmOpeAncDt;

    /**
     * 서류접수기간시작일
     * 예시: 2025-07-11
     */
    private Date pprAcpStDt;

    /**
     * 서류접수기간종료일
     * 예시: 2025-07-16
     */
    private Date pprAcpClsgDt;

    /**
     * 당첨자발표일
     * 예시: 2025-09-22
     */
    private Date pzwrAncDt;

    /**
     * 계약체결기간시작일
     * 예시: 2025-10-13
     */
    private Date ctrtStDt;

    /**
     * 계약체결기간종료일
     * 예시: 2025-10-15
     */
    private Date ctrtEdDt;
}
