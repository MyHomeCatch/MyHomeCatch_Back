package org.scoula.lh.dto.lhHousing;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.housing.LhHousingApplyVO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingApplyDTO {

    /**
     * 주택신청정보 ID (Primary Key, Auto Increment)
     */
    private Integer lhHousingApplyId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 구분 (청약 구분)
     * 예시: "사전청약당첨자", "다자녀특별(85㎡이하)", "신혼부부특별", "일반공급(우선)" 등
     */
    private String supplyType;

    /**
     * 신청일시
     * 예시: "2019.03.06 15:00 ~ 2020.03.04 18:00", "2025.07.14 10:00 ~ 2025.07.15 17:00"
     */
    private String applyDateRange;

    /**
     * 신청 시작일시 (파싱된 데이터)
     * acpDttm에서 "~" 앞부분을 파싱한 시작 날짜/시간
     */
    private Date applyStartDate;

    /**
     * 신청 종료일시 (파싱된 데이터)
     * acpDttm에서 "~" 뒷부분을 파싱한 종료 날짜/시간
     */
    private Date applyEndDate;

    /**
     * 신청방법
     * 예시: "인터넷접수"
     */
    private String applyMethod;

    /**
     * 당첨자 발표 일자
     * 예시: 2025-08-05
     */
    private Date annouceDate;

    /**
     * 당첨자서류제출 기간 시작일
     * 예시: 2025-08-11
     */
    private Date docSubmitStartDate;

    /**
     * 당첨자서류제출 기간 종료일
     * 예시: 2025-08-19
     */
    private Date docSubmitEndDate;

    /**
     * 계약체결 기간 시작일
     * 예시: 2025-10-27
     */
    private Date contractStartDate;

    /**
     * 계약체결 기간 종료일
     * 예시: 2025-10-31
     */
    private Date contractEndDate;

    public static LhHousingApplyDTO of(LhHousingApplyVO vo) {
        return LhHousingApplyDTO.builder()
                .lhHousingApplyId(vo.getLhHousingApplyId())
                .panId(vo.getPanId())
                .supplyType(vo.getHsSbscAcpTrgCdNm())
                .applyDateRange(vo.getAcpDttm())
                .applyStartDate(vo.getStartDttm())
                .applyEndDate(vo.getEndDttm())
                .applyMethod(vo.getRmk())
                .annouceDate(vo.getPzwrAncDt())
                .docSubmitStartDate(vo.getPzwrPprSbmStDt())
                .docSubmitEndDate(vo.getPzwrPprSbmEdDt())
                .contractStartDate(vo.getCtrtStDt())
                .contractEndDate(vo.getCtrtEdDt())
                .build();
    }

    /**
     * LhHousingApplyDTO를 LhHousingApplyVO로 변환
     * @return LhHousingApplyVO 객체
     */
    public LhHousingApplyVO toVO() {
        return LhHousingApplyVO.builder()
                .lhHousingApplyId(this.lhHousingApplyId)
                .panId(this.panId)
                .hsSbscAcpTrgCdNm(this.supplyType)
                .acpDttm(this.applyDateRange)
                .startDttm(this.applyStartDate)
                .endDttm(this.applyEndDate)
                .rmk(this.applyMethod)
                .pzwrAncDt(this.annouceDate)
                .pzwrPprSbmStDt(this.docSubmitStartDate)
                .pzwrPprSbmEdDt(this.docSubmitEndDate)
                .ctrtStDt(this.contractStartDate)
                .ctrtEdDt(this.contractEndDate)
                .build();
    }
}