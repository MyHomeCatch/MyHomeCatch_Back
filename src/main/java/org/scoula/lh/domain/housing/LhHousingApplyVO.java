package org.scoula.lh.domain.housing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * LH 주택 신청 정보 VO
 * LH_housing_apply 테이블과 매핑
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingApplyVO {

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
     * 신청 시작일시 (파싱된 데이터)
     * acpDttm에서 "~" 앞부분을 파싱한 시작 날짜/시간
     */
    private Date startDttm;

    /**
     * 신청 종료일시 (파싱된 데이터)
     * acpDttm에서 "~" 뒷부분을 파싱한 종료 날짜/시간
     */
    private Date endDttm;

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
}