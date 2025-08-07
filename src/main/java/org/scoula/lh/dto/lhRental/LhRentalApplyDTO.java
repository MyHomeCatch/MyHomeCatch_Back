package org.scoula.lh.dto.lhRental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.rental.LhRentalApplyVO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalApplyDTO {

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
    private String houseName;

    /**
     * 접수기간시작일
     * 예시: "2025.07.07"
     */
    private String applyStartDate;

    /**
     * 접수기간종료일
     * 예시: "2025.07.09"
     */
    private String applyEndDate;

    /**
     * 서류제출대상자발표일
     * 예시: "2025.07.11"
     */
    private String docSubmitTargetAnnounceDate;

    /**
     * 서류접수기간시작일
     * 예시: 2025-07-11
     */
    private Date docSubmitStartDate;

    /**
     * 서류접수기간종료일
     * 예시: 2025-07-16
     */
    private Date docSubmitEndDate;

    /**
     * 당첨자발표일
     * 예시: 2025-09-22
     */
    private Date announceDate;

    /**
     * 계약체결기간시작일
     * 예시: 2025-10-13
     */
    private Date contractStartDate;

    /**
     * 계약체결기간종료일
     * 예시: 2025-10-15
     */
    private Date contractEndDate;

    /**
     * LhRentalApplyVO를 LhRentalApplyDTO로 변환
     * @param vo LhRentalApplyVO 객체
     * @return LhRentalApplyDTO 객체
     */
    public static LhRentalApplyDTO of(LhRentalApplyVO vo) {
        return LhRentalApplyDTO.builder()
                .lhRentalId(vo.getLhRentalId())
                .panId(vo.getPanId())
                .houseName(vo.getSbdLgoNm())
                .applyStartDate(vo.getSbscAcpStDt())
                .applyEndDate(vo.getSbscAcpClsgDt())
                .docSubmitTargetAnnounceDate(vo.getPprSbmOpeAncDt())
                .docSubmitStartDate(vo.getPprAcpStDt())
                .docSubmitEndDate(vo.getPprAcpClsgDt())
                .announceDate(vo.getPzwrAncDt())
                .contractStartDate(vo.getCtrtStDt())
                .contractEndDate(vo.getCtrtEdDt())
                .build();
    }


    /**
     * LhRentalApplyDTO를 LhRentalApplyVO로 변환
     * @return LhRentalApplyVO 객체
     */
    public LhRentalApplyVO toVO() {
        return LhRentalApplyVO.builder()
                .lhRentalId(this.lhRentalId)
                .panId(this.panId)
                .sbdLgoNm(this.houseName)
                .sbscAcpStDt(this.applyStartDate)
                .sbscAcpClsgDt(this.applyEndDate)
                .pprSbmOpeAncDt(this.docSubmitTargetAnnounceDate)
                .pprAcpStDt(this.docSubmitStartDate)
                .pprAcpClsgDt(this.docSubmitEndDate)
                .pzwrAncDt(this.announceDate)
                .ctrtStDt(this.contractStartDate)
                .ctrtEdDt(this.contractEndDate)
                .build();
    }
}
