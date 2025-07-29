package org.scoula.selfCheck.service;

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

        boolean isQualified = "예".equals(dto.getIsHomeless()) &&
                "월평균 소득 70% 이하".equals(dto.getMonthlyIncome()) &&
                !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        String resultText;

        if (isQualified) {
            if (dto.getTargetGroups().stream().anyMatch(group ->
                    Arrays.asList("철거민", "장애인", "다자녀 가구", "국가유공자", "영구임대퇴거자",
                            "비닐간이공작물 거주자", "신혼부부", "한부모가족", "무허가 건축물 등에 입주한 세입자").contains(group))) {
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
            resultText = "행복주택 불가능 (무주택자 아님)";
            result.put("qualified", resultText);
            return result;
        }

        // 가구원 정보 파싱
        String[] householdInfo = dto.getHouseHoldMembers().split(",");
        int householdMembers = Integer.parseInt(householdInfo[0]);
        int childrenCount = householdInfo.length > 1 ? Integer.parseInt(householdInfo[1]) : 0;

        // 소득 조건 - 프론트엔드에서 전송하는 값들에 맞춤
        List<String> allowedIncomes = Arrays.asList(
                "월평균 소득 70% 이하",
                "월평균 소득 80% 이하",
                "월평균 소득 90% 이하",
                "월평균 소득 100% 이하",
                "월평균 소득 110% 이하",
                "월평균 소득 120% 이하"
        );
        boolean incomeEligible = allowedIncomes.contains(dto.getMonthlyIncome());
        boolean assetEligible = !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        for (String group : dto.getTargetGroups()) {
            switch (group) {
                case "청년 계층":
                case "신혼부부":
                case "한부모 가족":
                case "고령자 계층":
                case "대학생 계층":
                    if (incomeEligible && assetEligible) {
                        resultText = "행복주택 가능 (" + group + ")";
                        result.put("qualified", resultText);
                        saveResultIfQualified(userId, resultText);
                        return result;
                    } else {
                        resultText = "행복주택 불가능 (" + group + " 기준 불충족)";
                        result.put("qualified", resultText);
                        return result;
                    }
                case "주거급여수급자계층":
                case "기초생활수급자":
                case "위안부 피해자":
                case "북한이탈주민":
                case "아동복지시설 퇴소자":
                case "고령 저소득자":
                    resultText = "행복주택 우선공급 (" + group + ")";
                    result.put("qualified", resultText);
                    saveResultIfQualified(userId, resultText);
                    return result;
            }
        }

        resultText = "행복주택 불가능 (해당 계층 아님)";
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

        if (!(isHomeless && isEligibleRealEstate && isEligibleCar && hasValidSavingPeriod)) {
            resultText = "공공분양 불가능";
            result.put("qualified", resultText);
            return result;
        }

        List<String> targets = dto.getTargetGroups();

        if (targets.contains("다자녀 가구")) {
            resultText = "공공분양 다자녀 특별공급 대상";
        } else if (targets.contains("노부모부양")) {
            resultText = "공공분양 노부모부양 특별공급 대상";
        } else if (targets.contains("신혼부부") || targets.contains("한부모 가족")) {
            resultText = "공공분양 신혼부부 특별공급 대상";
        } else if (targets.contains("생애최초")) {
            resultText = "공공분양 생애최초 특별공급 대상";
        } else if (targets.contains("신생아")) {
            resultText = "공공분양 신생아 특별공급 대상";
        } else if (targets.contains("국가유공자")) {
            resultText = "공공분양 국가유공자 특별공급 대상";
        } else if (targets.contains("기관추천")) {
            resultText = "공공분양 기관추천 특별공급 대상";
        } else {
            resultText = "공공분양 일반공급 가능";
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
        // 소득 조건 - 프론트엔드에서 전송하는 값들에 맞춤
        boolean incomeOk = income.contains("70% 이하") || income.contains("60% 이하") ||
                income.contains("50% 이하") || income.contains("90% 이하") ||
                income.contains("80% 이하");
        boolean assetOk = !"총 자산 33,700만원 초과".equals(dto.getTotalAssets()) &&
                !"자동차 3,803만원 초과".equals(dto.getCarValue());

        List<String> groups = dto.getTargetGroups();
        List<String> firstPriorityGroups = Arrays.asList(
                "기초생활수급자", "국가유공자", "5.18 민주유공자", "특수임무수행자", "참전유공자",
                "위안부 피해자", "한부모 가족", "북한이탈주민", "장애인", "고령 저소득자", "아동복지시설 퇴소자"
        );

        String resultText;

        if (!isHomeless) {
            resultText = "영구임대 불가능 (무주택 아님)";
        } else if (groups.stream().anyMatch(firstPriorityGroups::contains) && incomeOk && assetOk) {
            resultText = "영구임대 1순위 입주 가능";
        } else {
            resultText = "영구임대 불가능 (1순위 조건 미충족)";
        }

        result.put("qualified", resultText);
        saveResultIfQualified(userId, resultText);
        return result;
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