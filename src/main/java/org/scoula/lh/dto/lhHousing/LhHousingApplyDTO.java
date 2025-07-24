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
    private String hsSbscAcpTrgCdNm;

    /**
     * 신청일시
     * 예시: "2019.03.06 15:00 ~ 2020.03.04 18:00", "2025.07.14 10:00 ~ 2025.07.15 17:00"
     */
    private String acpDttm;

    /**
     * 신청방법
     * 예시: "인터넷접수"
     */
    private String rmk;

    /**
     * 당첨자 발표 일자
     * 예시: 2025-08-05
     */
    private Date pzwrAncDt;

    /**
     * 당첨자서류제출 기간 시작일
     * 예시: 2025-08-11
     */
    private Date pzwrPprSbmStDt;

    /**
     * 당첨자서류제출 기간 종료일
     * 예시: 2025-08-19
     */
    private Date pzwrPprSbmEdDt;

    /**
     * 계약체결 기간 시작일
     * 예시: 2025-10-27
     */
    private Date ctrtStDt;

    /**
     * 계약체결 기간 종료일
     * 예시: 2025-10-31
     */
    private Date ctrtEdDt;

    public static LhHousingApplyDTO of(LhHousingApplyVO vo) {
        return LhHousingApplyDTO.builder()
                .lhHousingApplyId(vo.getLhHousingApplyId())
                .panId(vo.getPanId())
                .hsSbscAcpTrgCdNm(vo.getHsSbscAcpTrgCdNm())
                .acpDttm(vo.getAcpDttm())
                .rmk(vo.getRmk())
                .pzwrAncDt(vo.getPzwrAncDt())
                .pzwrPprSbmStDt(vo.getPzwrPprSbmStDt())
                .pzwrPprSbmEdDt(vo.getPzwrPprSbmEdDt())
                .ctrtStDt(vo.getCtrtStDt())
                .ctrtEdDt(vo.getCtrtEdDt())
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
                .hsSbscAcpTrgCdNm(this.hsSbscAcpTrgCdNm)
                .acpDttm(this.acpDttm)
                .rmk(this.rmk)
                .pzwrAncDt(this.pzwrAncDt)
                .pzwrPprSbmStDt(this.pzwrPprSbmStDt)
                .pzwrPprSbmEdDt(this.pzwrPprSbmEdDt)
                .ctrtStDt(this.ctrtStDt)
                .ctrtEdDt(this.ctrtEdDt)
                .build();
    }

}