package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 사용자와 공고의 적합성 분석 결과
public class EligibilityResultDTO {
    private EligibilityStatus overallStatus; // 종합적인 신청 가능 여부
    @Builder.Default
    private Map<String, EligibilityStatus> detailedStatus = new HashMap<>();

    @Builder.Default
    private List<String> notes = new ArrayList<>();
    // 항목별 판단 이유 및 주의 문구

    @Builder.Default
    private List<String> types = new ArrayList<>();

    // Enum for Eligibility Status
    public enum EligibilityStatus {
        ELIGIBLE,      // 적합
        INELIGIBLE,    // 부적합
        NEEDS_REVIEW,  // 추가 확인 필요 (e.g., 완화 조건)
        NOT_APPLICABLE // 해당 없음 (e.g., 자동차가 없는데 자동차 기준이 있는 경우)
    }

    public void addNote(String note) {
        if (notes == null) notes = new ArrayList<>();
        notes.add(note);
    }
}
