package org.scoula.selfCheck.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfCheckContentDto {
    private int userId;
    private int residencePeriod;
    private String isHomeless;
    private String houseHoldMembers;
    private String maritalStatus;
    private String monthlyIncome;
    private String totalAssets;
    private String carValue;
    private String realEstateValue;
    private String subscriptionPeriod;
    private String targetGroups;
}