package org.scoula.lh.danzi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private String title;

    @JsonProperty("Overview")
    private List<SummaryItem> overview;
    @JsonProperty("Key Points")
    private List<SummaryItem> keyPoints;

    private List<SummaryItem>applicationRequirements; // 신청 자격 / 신청 자격 상세 조건

    @JsonSetter("Eligibility")
    public void setEligibility(List<SummaryItem> items) {
        if (items != null) applicationRequirements.addAll(items);
    }

    @JsonSetter("Detailed Eligibility Conditions")
    public void setDetailedEligibility(List<SummaryItem> items) {
        if (items != null) applicationRequirements.addAll(items);
    }

    @JsonProperty("Rental Conditions")
    private List<SummaryItem> rentalConditions;
    @JsonProperty("Income Criteria")
    private List<SummaryItem> incomeCriteria; // 소득 및 자산보유 기준(소득)
    @JsonProperty("Asset Criteria")
    private List<SummaryItem> assetCriteria; // 소득 및 자산보유 기준(자산)
    @JsonProperty("Selection Criteria")
    private List<SummaryItem> selectionCriteria; // 선정 기준 / 배점 기준
    @JsonProperty("Schedule")
    private List<SummaryItem> schedule; // 추진 일정
    @JsonProperty("Required Documents")
    private List<SummaryItem> requiredDocuments;  // 제출 서류
    @JsonProperty("Reference Links")
    private List<SummaryItem> referenceLinks;
    @JsonProperty("Target Groups")
    private List<SummaryItem> targetGroups;

    @Data
    public static class SummaryItem {
        private String group;
        private String evidence;
        @JsonProperty("source_section")
        private String sourceSection;
        @JsonProperty("source_heading")
        private String sourceHeading;
    }


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