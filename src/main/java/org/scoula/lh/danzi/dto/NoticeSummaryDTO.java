package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSummaryDTO {
    private int danziId; // 단지 ID
    private String applicationRequirements; // 신청 자격 / 신청 자격 상세 조건
    private String rentalConditions;        // 임대 조건 -- 공급 대상
    private String incomeConditions;        // 소득 및 자산보유 기준(소득)
    private String assetConditions;         // 소득 및 자산보유 기준(자산)
    private String selectionCriteria;       // 선정 기준 / 배점 기준
    private String schedule;                // 추진 일정
    private String requiredDocuments;       // 제출 서류

    private static String mapToDbColumn(String canon) {
        return switch (canon) {
            case "신청자격", "신청자격상세조건" -> "application_requirements";
            case "임대조건" -> "rental_conditions";
            case "소득기준" -> "income_conditions";
            case "자산기준" -> "asset_conditions";
            case "선정기준" -> "selection_criteria";
            case "추진일정" -> "schedule";
            case "제출서류" -> "required_documents";
            default -> null; // 저장 대상 아님
        };
    }
}