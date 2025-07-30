package org.scoula.selfCheck.service;

import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.scoula.selfCheck.dto.SelfCheckRequestDto;
import org.scoula.selfCheck.mapper.SelfCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SelfCheckServiceImpl implements SelfCheckService{

    @Autowired
    private SelfCheckMapper selfCheckMapper;

    @Override
    public Map<String, Object> evaluateKookMin(SelfCheckRequestDto dto, int userId) {
        Map<String, Object> result = new HashMap<>();

        // 가구원 정보 파싱 (예: "2,0" -> 세대원수: 2, 자녀수: 0)
        String[] householdInfo = dto.getHouseHoldMembers().split(",");
        int householdMembers = Integer.parseInt(householdInfo[0]);
        int childrenCount = householdInfo.length > 1 ? Integer.parseInt(householdInfo[1]) : 0;

        // 가구원 수에 따라 허용되는 소득 기준 정의
        List<String> allowedIncomeLevels;
        if (householdMembers == 1) {
            allowedIncomeLevels = Arrays.asList("월평균 소득 90% 이하", "월평균 소득 80% 이하", "월평균 소득 70% 이하");
        } else if (householdMembers == 2) {
            allowedIncomeLevels = Arrays.asList("월평균 소득 80% 이하", "월평균 소득 70% 이하");
        } else {
            allowedIncomeLevels = Collections.singletonList("월평균 소득 70% 이하");
        }

        boolean assetEligible = !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        boolean isQualified = "예".equals(dto.getIsHomeless()) &&
                allowedIncomeLevels.contains(dto.getMonthlyIncome()) &&
                assetEligible;

        String resultText;

        if (isQualified) {
            if (dto.getTargetGroups().stream().anyMatch(group ->
                    Arrays.asList("철거민", "장애인", "다자녀 가구", "국가유공자", "영구임대퇴거자",
                            "비닐간이공작물 거주자", "신혼부부", "한부모 가족", "무허가건축물 등에 입주한 세입자").contains(group))) {
                resultText = "국민임대 우선공급";
            } else {
                resultText = "국민임대 가능";
            }
        } else {
            resultText = "국민임대 불가능";
        }

        result.put("qualified", resultText);
        saveResultIfQualified(userId, resultText);
        return result;
    }

    @Override
    public Map<String, Object> evaluateHengBok(SelfCheckRequestDto dto, int userId) {
        Map<String, Object> result = new HashMap<>();
        String resultText;

        if (!"예".equals(dto.getIsHomeless())) {
            resultText = "행복주택 불가능";
            result.put("qualified", resultText);
            return result;
        }

        String[] householdInfo = dto.getHouseHoldMembers().split(",");
        int householdMembers = Integer.parseInt(householdInfo[0]);

        List<String> incomeScope = new ArrayList<>();
        if (householdMembers == 1) {
            incomeScope = Arrays.asList("월평균 소득 120% 이하", "월평균 소득 110% 이하", "월평균 소득 100% 이하", "월평균 소득 90% 이하", "월평균 소득 80% 이하", "월평균 소득 70% 이하");
        } else if (householdMembers == 2) {
            incomeScope = Arrays.asList("월평균 소득 110% 이하", "월평균 소득 100% 이하", "월평균 소득 90% 이하", "월평균 소득 80% 이하", "월평균 소득 70% 이하");
        } else {
            incomeScope = Arrays.asList("월평균 소득 100% 이하", "월평균 소득 90% 이하", "월평균 소득 80% 이하", "월평균 소득 70% 이하");
        }

        String income = dto.getMonthlyIncome();

        boolean assetEligible = !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        for (String group : dto.getTargetGroups()) {
            switch (group) {
                case "대학생 계층":
                case "청년 계층":
                case "고령자 계층":
                    if (incomeScope.contains(income) && assetEligible) {
                        resultText = "행복주택 가능";
                        result.put("qualified", resultText);
                        saveResultIfQualified(userId, resultText);
                        return result;
                    } else {
                        resultText = "행복주택 불가능";
                        result.put("qualified", resultText);
                        return result;
                    }

                case "신혼부부":
                case "한부모 가족":
                    // 맞벌이 여부 확인
                    boolean isDualIncome = "기혼(맞벌이)".equals(dto.getMaritalStatus());
                    List<String> extendedIncomeScope = new ArrayList<>(incomeScope);
                    if (isDualIncome) {
                        extendedIncomeScope.addAll(Arrays.asList("월평균 소득 120% 이하", "월평균 소득 110% 이하"));
                    }
                    if (extendedIncomeScope.contains(income) && assetEligible) {
                        resultText = "행복주택 가능";
                        result.put("qualified", resultText);
                        saveResultIfQualified(userId, resultText);
                        return result;
                    } else {
                        resultText = "행복주택 불가능";
                        result.put("qualified", resultText);
                        return result;
                    }

                case "주거급여수급자계층":
                case "기초생활수급자":
                case "위안부 피해자":
                case "북한이탈주민":
                case "아동복지시설 퇴소자":
                case "고령 저소득자":
                    resultText = "행복주택 우선공급";
                    result.put("qualified", resultText);
                    saveResultIfQualified(userId, resultText);
                    return result;
            }
        }

        resultText = "행복주택 불가능";
        result.put("qualified", resultText);
        return result;
    }

    @Override
    public Map<String, Object> evaluateGongGong(SelfCheckRequestDto dto, int userId) {
        Map<String, Object> result = new HashMap<>();

        boolean isHomeless = "예".equals(dto.getIsHomeless());
        boolean isEligibleRealEstate = !"부동산 21,550만원 초과".equals(dto.getRealEstateValue());
        boolean isEligibleCar = !"자동차 3,803만원 초과".equals(dto.getCarValue());

        String bankSavingPeriod = dto.getSubscriptionPeriod();
        boolean hasValidSavingPeriod = bankSavingPeriod != null && !bankSavingPeriod.equals("없음");

        String resultText;

        // 1단계: 기본 조건 불충족
        if (!(isHomeless && isEligibleRealEstate && isEligibleCar && hasValidSavingPeriod)) {
            resultText = "공공분양 불가능";
            result.put("qualified", resultText);
            return result;
        }

        // 2단계: 특별공급 여부 판단 - 프론트엔드 실제 선택지에 맞춤
        List<String> targets = dto.getTargetGroups();
        List<String> specialGroups = Arrays.asList(
                "다자녀 가구", "노부모부양", "신혼부부", "한부모 가족", "생애최초", "신생아", "국가유공자", "기관추천"
        );

        boolean isSpecialSupply = targets.stream().anyMatch(specialGroups::contains);

        if (isSpecialSupply) {
            resultText = "공공분양 특별공급";
        } else {
            resultText = "공공분양 가능";
        }

        if ("12개월 이상".equals(bankSavingPeriod) || "24개월 이상".equals(bankSavingPeriod)) {
            resultText += " (1순위)";
        }

        result.put("qualified", resultText);
        saveResultIfQualified(userId, resultText);
        return result;
    }

    @Override
    public Map<String, Object> evaluate09(SelfCheckRequestDto dto, int userId) {
        Map<String, Object> result = new HashMap<>();

        boolean isHomeless = "예".equals(dto.getIsHomeless());
        String income = dto.getMonthlyIncome();
        String[] householdInfo = dto.getHouseHoldMembers().split(",");
        int householdSize = Integer.parseInt(householdInfo[0]);

        // 자산 조건 - 프론트엔드 옵션에 맞춤
        boolean assetOk = !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        List<String> groups = dto.getTargetGroups();
        String resultText = "";

        if (!isHomeless) {
            resultText = "영구임대 불가능";
            result.put("qualified", resultText);
            return result;
        }

        for (String group : groups) {
            boolean incomeOk = false;
            if (householdSize == 1) {
                incomeOk = income.contains("90% 이하") || income.contains("80% 이하") || income.contains("70% 이하");
            } else if (householdSize == 2) {
                incomeOk = income.contains("80% 이하") || income.contains("70% 이하");
            } else {
                incomeOk = income.contains("70% 이하");
            }

            switch (group) {
                case "기초생활수급자", "고령 저소득자", "한부모 가족", "위안부 피해자":
                    resultText = "영구임대 가능 1순위";
                    break;
                case "국가유공자", "장애인", "북한이탈주민", "아동복지시설 퇴소자":
                    if (incomeOk && assetOk) {
                        resultText = "영구임대 가능 1순위";
                    }
                    break;
            }

            if (!resultText.isEmpty()) {
                result.put("qualified", resultText);
                saveResultIfQualified(userId, resultText);
                return result;
            }
        }

        resultText = "영구임대 불가능";
        result.put("qualified", resultText);
        return result;
    }

    @Override
    public void saveSelfCheckContent(SelfCheckRequestDto dto, int userId) {
        selfCheckMapper.insertSelfCheckContent(dto, userId, dto.getTargetGroupsStr());
    }

    @Override
    public void deleteSelfCheckContent(int userId) {
        selfCheckMapper.deleteSelfCheckContentByUserId(userId);
    }

    @Override
    public SelfCheckContentDto getSelfCheckContent(int userId) {
        return selfCheckMapper.findSelfCheckContentByUserId(userId);
    }

    private void saveResultIfQualified(int userId, String result) {
        // 자격이 있는 경우만 저장 (불가능이 아닌 경우)
        if (!result.contains("불가능")) {
            selfCheckMapper.insertResult(userId, result);
        }
    }

    private void clearPreviousResults(int userId) {
        selfCheckMapper.deleteByUserId(userId);
    }
}