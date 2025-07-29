package org.scoula.selfCheck.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfCheckRequestDto {
    private int residencePeriod; // 거주기간
    private String isHomeless; // 무주택
    private String houseHoldMembers; // 가족수
    private String maritalStatus; // 미혼 기혼
    private String monthlyIncome; // 월 소득
    private String totalAssets; // 총 재산
    private String carValue; // 차 가격
    private String realEstateValue; // 부동산
    private String subscriptionPeriod; // 청약 가입 기간
    private List<String> targetGroups; // 해당사항
}
