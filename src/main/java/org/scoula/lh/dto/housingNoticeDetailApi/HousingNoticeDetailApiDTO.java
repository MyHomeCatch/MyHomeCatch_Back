package org.scoula.lh.dto.housingNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 메인 응답 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HousingNoticeDetailApiDTO {
    /**
     * 공급일정 정보
     * - 청약 접수 일정, 당첨자 발표일, 계약 기간 등 분양 관련 일정 정보
     * 예시: "사전청약당첨자", "다자녀특별(85㎡이하)", "신혼부부특별", "일반공급(우선)" 등의 구분별 일정
     * - ACP_DTTM: "2025.07.14 10:00 ~ 2025.07.15 17:00" (접수일시)
     * - PZWR_ANC_DT: "20250805" (당첨자발표일)
     * - CTRT_ST_DT/CTRT_ED_DT: "20251027"~"20251031" (계약기간)
     */
    @JsonProperty("dsSplScdl")
    private List<DsSplScdlDTO> dsSplScdl;

    /**
     * 기타 정보
     * - 공고의 상세 내용 및 기타 안내사항
     * 예시:
     * - ETC_FCTS: "주택전시관 주차공간이 협소하니 대중교통 이용을 권장드립니다."
     * - PAN_DTL_CTS: "정정사항", "입주자모집공고일", "공급위치", "공급대상" 등의 상세 공고 내용
     */
    @JsonProperty("dsEtcInfo")
    private List<DsEtcInfoDTO> dsEtcInfo;

    /**
     * 첨부파일 정보
     * - 공고문, 팸플릿, 각종 서류 양식 등의 다운로드 가능한 파일 정보
     * 예시:
     * - CMN_AHFL_NM: "(정정)고양장항_S-1블록_팸플릿.pdf", "위임장.hwp" 등
     * - SL_PAN_AHFL_DS_CD_NM: "정정카탈로그", "기타 첨부파일", "정정공고문(PDF)" 등
     * - AHFL_URL: 실제 파일 다운로드 URL
     */
    @JsonProperty("dsAhflInfo")
    private List<DsAhflInfoDTO> dsAhflInfo;

    /**
     * 단지 기본 정보
     * - 단지의 위치, 규모, 시설, 입주 예정일 등 기본 정보
     * 예시:
     * - BZDT_NM: "고양장항 S-1" (단지명)
     * - LCT_ARA_ADR: "경기도 고양시 일산동구 장항동" (위치)
     * - MIN_MAX_RSDN_DDO_AR: "59.92 ~ 84.97" (전용면적 범위)
     * - SUM_TOT_HSH_CNT: "869" (총 세대수)
     * - MVIN_XPC_YM: "2028년 03월" (입주예정월)
     */
    @JsonProperty("dsSbd")
    private List<DsSbdDTO> dsSbd;

    /**
     * 단지 첨부파일 정보
     * - 단지 관련 이미지 파일들 (조감도, 배치도, 위치도 등)
     * 예시:
     * - CMN_AHFL_NM: "고양장항S-1블록_지역조감도_고.jpg", "고양장항S-1블록_단지배치도_고.jpg" 등
     * - SL_PAN_AHFL_DS_CD_NM: "지역위치도", "단지조감도", "단지배치도", "토지이용계획도" 등
     * - AHFL_URL: 이미지 파일 조회 URL
     */
    @JsonProperty("dsSbdAhfl")
    private List<DsSbdAhflDTO> dsSbdAhfl;

    /**
     * 계약장소 정보
     * - 주택전시관 위치, 운영시간, 연락처 등 실제 방문 관련 정보
     * 예시:
     * - CTRT_PLC_ADR: "경기도 파주시 와동동" (주소)
     * - SIL_OFC_TLNO: "(031)941-0998" (전화번호)
     * - SIL_OFC_DT: "2025.07.05~2025.07.13 (10:00~17:00)" (운영기간)
     * - SIL_OFC_GUD_FCTS: 주택전시관 관람 안내사항
     */
    @JsonProperty("dsCtrtPlc")
    private List<DsCtrtPlcDTO> dsCtrtPlc;
}
