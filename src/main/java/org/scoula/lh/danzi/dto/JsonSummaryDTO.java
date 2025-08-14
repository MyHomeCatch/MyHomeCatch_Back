package org.scoula.lh.danzi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonSummaryDTO {

    private int danziId; // 단지 ID
    private String title;

    @JsonProperty("Overview")
    private List<SummaryItem> overview;
    @JsonProperty("Key Points")
    private List<SummaryItem> keyPoints;

    @JsonProperty("Application Requirements")
    @Builder.Default
    private List<SummaryItem> applicationRequirements = new ArrayList<>(); // 신청 자격 / 신청 자격 상세 조건

    @JsonSetter("Eligibility")
    public void setEligibility(List<SummaryItem> items) {
        if (items != null && !items.isEmpty()) {
            if (this.applicationRequirements == null) this.applicationRequirements = new ArrayList<>();
            this.applicationRequirements.addAll(items);
        }
    }

    @JsonSetter("Detailed Eligibility Conditions")
    public void setDetailedEligibility(List<SummaryItem> items) {
        if (items != null && !items.isEmpty()) {
            if (this.applicationRequirements == null) this.applicationRequirements = new ArrayList<>();
            this.applicationRequirements.addAll(items);
        }
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
    }

}