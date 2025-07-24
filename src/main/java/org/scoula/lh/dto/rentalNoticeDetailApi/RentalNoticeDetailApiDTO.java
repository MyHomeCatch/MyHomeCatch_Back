package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 임대주택 공고 상세 정보 메인 응답 DTO
 * - 행복주택, 국민임대, 기타 임대주택 등에 사용
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalNoticeDetailApiDTO {
    /**
     * 공급일정 정보
     * - 청약 접수 일정, 당첨자 발표일, 계약 기간 등 분양 관련 일정 정보
     * - PPR_SBM_OPE_ANC_DT: "2025.07.11" (서류제출대상자발표일)
     * - PZWR_ANC_DT: "2025.09.22" (당첨자발표일)
     * - CTRT_ST_DT/CTRT_ED_DT: "2025.10.13"~"2025.10.15" (계약기간)
     */
    @JsonProperty("dsSplScdl")
    private List<RentalDsSplScdlDTO> dsSplScdl;

    /**
     * 단지 첨부파일명 헤더 정보
     * - 첨부파일 테이블의 컬럼명 정보 (실제 데이터가 아닌 헤더)
     * 예시: "단지명", "다운로드", "파일구분명", "첨부파일명"
     */
    @JsonProperty("dsSbdAhflNm")
    private List<RentalDsSbdAhflNmDTO> dsSbdAhflNm;

    /**
     * 기타 정보
     * - 공고의 상세 내용 및 기타 안내사항
     * 예시:
     * - ETC_CTS: "금회 모집 예비입주자는 현재 공가 및 향후 해약이 발생할 경우를 대비하여 모집하는 것으로..."
     * - CRC_RSN: 정정/취소사유
     */
    @JsonProperty("dsEtcInfo")
    private List<RentalDsEtcInfoDTO> dsEtcInfo;

    /**
     * 첨부파일 정보
     * - 공고문, 팸플릿, 각종 서류 양식 등의 다운로드 가능한 파일 정보
     * 예시:
     * - CMN_AHFL_NM: "6-3M중1행복주택팸플렛.pdf", "위임장.hwp" 등
     * - SL_PAN_AHFL_DS_CD_NM: "카탈로그", "기타 첨부파일", "공고문(PDF)" 등
     * - AHFL_URL: 실제 파일 다운로드 URL
     */
    @JsonProperty("dsAhflInfo")
    private List<RentalDsAhflInfoDTO> dsAhflInfo;

    /**
     * 단지 기본 정보
     * - 단지의 위치, 규모, 시설, 입주 예정일 등 기본 정보
     * 예시:
     * - LCC_NT_NM: "(세종)행정중심복합도시 6-3M중1블록 행복주택" (단지명)
     * - LGDN_ADR: "세종특별자치시 산울동" (위치)
     * - DDO_AR: "21.59~36.9" (전용면적 범위)
     * - HSH_CNT: "238" (총 세대수)
     * - MVIN_XPC_YM: "2025.11" (입주예정월)
     */
    @JsonProperty("dsSbd")
    private List<RentalDsSbdDTO> dsSbd;

    /**
     * 단지 첨부파일 정보
     * - 단지 관련 이미지 파일들 (조감도, 배치도, 위치도 등)
     * 예시:
     * - CMN_AHFL_NM: "6-3M중1_단지조감도.png", "6-3M중1_단지배치도.png" 등
     * - LS_SPL_INF_UPL_FL_DS_CD_NM: "단지조감도", "단지배치도", "동호배치도" 등
     * - AHFL_URL: 이미지 파일 조회 URL
     */
    @JsonProperty("dsSbdAhfl")
    private List<RentalDsSbdAhflDTO> dsSbdAhfl;

    /**
     * 계약장소 정보
     * - 접수처 위치, 운영시간, 연락처 등 실제 방문 관련 정보
     * 예시:
     * - CTRT_PLC_ADR: "세종특별자치시 가름로 238-3(어진동)" (주소)
     * - SIL_OFC_TLNO: "1600-1004" (전화번호)
     * - TSK_ST_DTTM/TSK_ED_DTTM: "2027.07.07"~"2027.07.09" (운영기간)
     */
    @JsonProperty("dsCtrtPlc")
    private List<RentalDsCtrtPlcDTO> dsCtrtPlc;
}