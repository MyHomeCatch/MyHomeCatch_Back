package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.dto.EligibilityResultDTO;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static org.scoula.lh.danzi.dto.EligibilityResultDTO.EligibilityStatus.*;

@Service
public class EligibilityServiceImpl implements EligibilityService {
    List<String> notes = new ArrayList<>();

    private void note(String category, String msg) {
        if (msg == null || msg.isEmpty()) return;
        notes.add("[" + category + "] " + msg);
    }

    public EligibilityResultDTO analyze(NoticeSummaryDTO summary, SelfCheckContentDto user) {
        notes = new ArrayList<>();
        Map<String, EligibilityResultDTO.EligibilityStatus> detail = new LinkedHashMap<>();

        // 1) 무주택
        EligibilityResultDTO.EligibilityStatus homeless =
                evalHomeless(summary.getApplicationRequirements(), user);
        detail.put("homeless", homeless);
        note("무주택", "판정=" + homeless);
        if (homeless == NEEDS_REVIEW) notes.add("무주택요건 완화 문구가 있어 확인이 필요합니다.");

        // 2) 소득
        EligibilityResultDTO.EligibilityStatus income =
                evalIncome(summary.getIncomeConditions(), user);
        detail.put("income", income);

        note("소득", "판정=" + income);

        // 3) 총자산/부동산
        EligibilityResultDTO.EligibilityStatus asset =
                evalAssets(summary.getAssetConditions(), user);
        detail.put("asset", asset);
        note("자산", "판정=" + asset);

        // 4) 자동차가액
        EligibilityResultDTO.EligibilityStatus car =
                evalCar(summary, user);
        detail.put("car", car);
        note("자동차", "판정=" + car);

        // 5) 공급 대상
        List<String> correspondents = evalTypes(summary, user);
        if (correspondents.size() == 0) {
            detail.put("correspondents", INELIGIBLE);
            note("대상", "판정=" + INELIGIBLE);
        } else {
            detail.put("correspondents", ELIGIBLE);
            note("대상", "판정=" + ELIGIBLE);
        }

        // 간단 종합: 하나라도 INELIGIBLE이면 INELIGIBLE,
        // 모두 ELIGIBLE/NOT_APPLICABLE이면 ELIGIBLE,
        // 그 외는 NEEDS_REVIEW
        EligibilityResultDTO.EligibilityStatus overall = ELIGIBLE;
        boolean anyReview = false;
        for (EligibilityResultDTO.EligibilityStatus st : detail.values()) {
            if (st == INELIGIBLE) {
                overall = INELIGIBLE;
                break;
            }
            if (st == NEEDS_REVIEW) anyReview = true;
        }
        if (overall != INELIGIBLE && anyReview) overall = NEEDS_REVIEW;

        EligibilityResultDTO result = new EligibilityResultDTO();
        result.setDetailedStatus(detail);
        result.setOverallStatus(overall);
        result.setTypes(correspondents);
        result.setNotes(notes);

        return result;
    }

    public EligibilityResultDTO.EligibilityStatus evalHomeless(String text, SelfCheckContentDto u) {
        note("무주택", "입력 체크: user=" + (u != null) + ", isHomeless=" + (u != null ? u.getIsHomeless() : null));
        if (u == null || u.getIsHomeless() == null) {
            note("무주택", "사용자 무주택 여부가 비어있어 추가 확인 필요");
            return NEEDS_REVIEW;
        }
        if (text == null) {
            note("무주택", "공고문 텍스트 없음 → 판정 보류");
            notes.add("공고문에서 무주택 관련 내용이 확인되지 않아 직접 공고문을 확인해주시길 바랍니다.");
            return NEEDS_REVIEW;
        }

        String t = text;
        boolean requiresHomeless = t.contains("무주택세대구성원") || t.contains("무주택 세대구성원") || t.contains("무주택");
        boolean relaxed = t.contains("무주택요건 완화") || t.contains("소형·저가주택") || t.contains("무주택으로 간주");
        note("무주택", "요건존재=" + requiresHomeless + ", 완화문구=" + relaxed);

        if (!requiresHomeless) {
            note("무주택", "무주택 요건 비명시 → 해당 없음 처리");
            notes.add("신청 자격에 무주택 관련이 없는 것으로 보입니다.");
            return NOT_APPLICABLE;
        }

        if (u.getIsHomeless().equals("예")) {
            note("무주택", "사용자 응답=예 (무주택)");
            notes.add("무주택 요건: 사용자가 무주택이므로 적격입니다.");
            return ELIGIBLE;
        }
        if (relaxed) {
            note("무주택", "사용자 무주택 아님, 단 완화 문구 존재 → 추가 확인 필요");
            return NEEDS_REVIEW;
        } else {
            note("무주택", "사용자 무주택 아님 → 부적격");
            return INELIGIBLE;
        }
    }

    public EligibilityResultDTO.EligibilityStatus evalIncome(String text, SelfCheckContentDto u) {
        note("소득", "입력 체크: user=" + (u != null) + ", monthlyIncome=" + (u != null ? u.getMonthlyIncome() : null));
        if (text == null) {
            note("소득", "공고문 소득 항목 없음 → 판정 보류");
            return NEEDS_REVIEW;
        }
        if (text.contains("소득 요건 배제") || text.contains("소득 배제")) {
            note("소득", "공고에서 '소득 요건 배제' 명시 → 해당 없음");
            return NOT_APPLICABLE;
        }
        if (u == null || u.getMonthlyIncome() == null) {
            note("소득", "사용자 소득 입력 없음 → 판정 보류");
            return NEEDS_REVIEW;
        }

        Integer limit = extractMaxPercent(text); // e.g. "120% 이하" → 120
        note("소득", "공고 상한% 파싱 값은 " + limit + "이에요.");
        if (limit == null) return NEEDS_REVIEW;
        Integer userPct = extractMaxPercent(u.getMonthlyIncome());
        note("소득", "사용자 소득% 은 " + userPct + "로 추정돼요.");
        if (userPct == null) {
            note("소득", "사용자 퍼센트 파싱 실패 → 판정 보류");
            return NEEDS_REVIEW;
        }
        if (userPct <= limit) {
            note("소득", "비교: 사용자=" + userPct + "% ≤ 기준=" + limit + "% → 적격");
            return ELIGIBLE;
        } else {
            note("소득", "비교: 사용자=" + userPct + "% > 기준=" + limit + "% → 부적격");
            return INELIGIBLE;
        }
    }

    public EligibilityResultDTO.EligibilityStatus evalAssets(String text, SelfCheckContentDto u) {
        note("자산", "입력 체크: user=" + (u != null) + ", totalAssets=" + (u != null ? u.getTotalAssets() : null));
        if (text == null) {
            note("자산", "공고문 자산 항목 없음 → 판정 보류");
            return NEEDS_REVIEW;
        }
        if (text.contains("총자산 요건 배제") || text.contains("총자산 배제") || text.contains("자산 요건 배제")) {
            note("자산", "공고에서 '자산 요건 배제' 명시 → 해당 없음");
            return NOT_APPLICABLE;
        }
        if (u == null || u.getTotalAssets() == null) {
            note("자산", "사용자 자산 입력 없음 → 판정 보류");
            return NEEDS_REVIEW;
        }

        Integer limitAsset = extractFirstMoneyManwon(text); // 맨 앞 금액 하나를 총자산 기준으로 가정
        note("자산", "공고 총자산 상한(만원) 파싱=" + limitAsset);
        if (limitAsset == null) {
            note("자산", "총자산 상한 파싱 실패 → 판정 보류");
            return NEEDS_REVIEW;
        }
        Integer userAsset = extractFirstMoneyManwon(u.getTotalAssets());
        note("자산", "사용자 총자산(만원) 파싱=" + userAsset);
        if (userAsset == null) {
            note("자산", "사용자 총자산 파싱 실패 → 판정 보류");
            return NEEDS_REVIEW;
        }
        if (userAsset <= limitAsset) {
            note("자산", "비교: 사용자=" + userAsset + " ≤ 기준=" + limitAsset + " → 적격");
            return ELIGIBLE;
        } else {
            note("자산", "비교: 사용자=" + userAsset + " > 기준=" + limitAsset + " → 부적격");
            return INELIGIBLE;
        }
    }

    public EligibilityResultDTO.EligibilityStatus evalCar(NoticeSummaryDTO summary, SelfCheckContentDto u) {
        note("자동차", "입력 체크: user=" + (u != null) + ", carValue=" + (u != null ? u.getCarValue() : null));
        String all = joinNonNull(summary.getIncomeConditions(), summary.getAssetConditions(),
                summary.getApplicationRequirements(), summary.getRentalConditions());
        if (all == null) {
            note("자동차", "관련 텍스트 부재 → 판정 보류");
            return NEEDS_REVIEW;
        }

        boolean mentionsCar = all.contains("자동차가액") || all.contains("차량가액") || all.contains("자동차 기준");
        note("자동차", "문구존재=" + mentionsCar);
        if (!mentionsCar) {
            note("자동차", "자동차 기준 미명시 → 해당 없음");
            return NOT_APPLICABLE;
        }
        if (all.contains("자동차가액은 3,803만원 이하") || all.contains("3,803만원 이하")) {
            note("자동차", "기준=3,803만원 이하로 탐지");
            if (u == null || u.getCarValue() == null) {
                note("자동차", "사용자 자동차가액 입력 없음 → 판정 보류");
                return NEEDS_REVIEW;
            }
            Integer userCar = extractFirstMoneyManwon(u.getCarValue());
            note("자동차", "사용자 자동차가액(만원) 파싱=" + userCar);
            if (userCar == null) {
                note("자동차", "사용자 자동차가액 파싱 실패 → 판정 보류");
                return NEEDS_REVIEW;
            }
            if (userCar <= 3803) {
                note("자동차", "비교: 사용자=" + userCar + " ≤ 기준=3803 → 적격");
                return ELIGIBLE;
            } else {
                note("자동차", "비교: 사용자=" + userCar + " > 기준=3803 → 부적격");
                return INELIGIBLE;
            }
        }
        note("자동차", "자동차 문구는 있으나 구체 금액 탐지 실패 → 판정 보류");
        return NEEDS_REVIEW;
    }

    public List<String> evalTypes(NoticeSummaryDTO summary, SelfCheckContentDto u) {
        List<String> types = new ArrayList<>();
        String text = joinNonNull(
                summary != null ? summary.getRentalConditions() : null,
                summary != null ? summary.getApplicationRequirements() : null,
                summary != null ? summary.getSelectionCriteria() : null
        );

        if (text == null) {
            note("대상", "대상 추출용 텍스트 없음 → 빈 목록 반환");
            return types;
        }

        // 공백 normalize (키워드 매칭 안정화)
        String norm = text.replaceAll("\\s+", " ");

        // target_groups 표준 키 → 키워드(동의어) 매핑 (삽입 순서가 곧 결과 정렬 순서)
        Map<String, String[]> dict = new LinkedHashMap<>();
        dict.put("철거민", new String[]{"철거민"});
        dict.put("장애인", new String[]{"장애인", "장애 정도", "장애증명"});
        dict.put("다자녀 가구", new String[]{"다자녀"});
        dict.put("국가유공자", new String[]{"국가유공자", "민주유공자", "5.18", "보훈", "고엽제후유의증"});
        dict.put("영구임대퇴거자", new String[]{"영구임대퇴거자"});
        dict.put("비닐간이공작물 거주자", new String[]{"비닐간이공작물"});
        dict.put("신혼부부", new String[]{"신혼부부", "예비신혼부부"});
        dict.put("한부모 가족", new String[]{"한부모가족", "한부모"});
        dict.put("무허가건축물 등에 입주한 세입자", new String[]{"무허가건축물"});
        dict.put("기관추천", new String[]{"기관추천"});
        dict.put("신생아", new String[]{"신생아"});
        dict.put("생애최초", new String[]{"생애최초"});
        dict.put("노부모부양", new String[]{"노부모부양", "노부모 부양"});
        dict.put("대학생 계층", new String[]{"대학생", "취업준비생"});
        dict.put("청년 계층", new String[]{"청년", "사회초년생"});
        dict.put("고령자 계층", new String[]{"고령자", "65세 이상", "만65세", "만 65세"});
        dict.put("주거급여수급자계층", new String[]{"주거급여수급자", "주거급여 수급자", "주거급여 수급권자"});
        dict.put("기초생활수급자", new String[]{"기초생활수급자", "생계급여", "의료급여"});
        dict.put("위안부 피해자", new String[]{"위안부 피해자"});
        dict.put("북한이탈주민", new String[]{"북한이탈주민"});
        dict.put("아동복지시설 퇴소자", new String[]{"아동복지시설 퇴소자"});
        dict.put("고령 저소득자", new String[]{"고령 저소득자", "저소득 고령자"});
        dict.put("해당 없음", new String[]{"해당 없음"});

        // 중복 방지를 위한 집합 (표준 키 순서 유지)
        java.util.LinkedHashSet<String> hits = new java.util.LinkedHashSet<>();

        for (Map.Entry<String, String[]> e : dict.entrySet()) {
            String canonical = e.getKey();
            String[] kws = e.getValue();
            boolean matched = false;
            for (String kw : kws) {
                if (kw == null || kw.isEmpty()) continue;
                if (norm.contains(kw)) {
                    matched = true;
                    break;
                }
            }
            if (matched) {
                hits.add(canonical);
                note("대상", "탐지: '" + canonical + "'");
            }
        }

        // '해당 없음'은 다른 대상이 하나도 없고 문구가 있을 때만 추가
        if (hits.contains("해당 없음") && hits.size() > 1) {
            hits.remove("해당 없음");
        }

        types.addAll(hits);
        note("대상", "추출 결과=" + types);
        return types;
    }

    public Integer extractMaxPercent(String text) {
        Matcher m = java.util.regex.Pattern.compile("(\\d{1,3})\\s*%\\s*이하").matcher(text);
        Integer max = null;
        while (m.find()) {
            int v = Integer.parseInt(m.group(1));
            if (max == null || v > max) max = v;
        }
        if (max == null) {
            note("parser%", "퍼센트 값 파싱 실패: text=" + (text != null ? Math.min(text.length(), 80) : 0));
        }
        return max;
    }

    public Integer extractFirstMoneyManwon(String text) {
        Matcher m = java.util.regex.Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)\\s*만원\\s*이하").matcher(text);
        if (m.find()) {
            String digits = m.group(1).replace(",", "");
            try {
                return Integer.parseInt(digits);
            } catch (NumberFormatException e) {
                note("parser₩", "만원 이하 숫자 파싱 실패: " + digits);
            }
        }
        Matcher m2 = java.util.regex.Pattern.compile("([0-9]{2,4})\\s*백만원").matcher(text);
        if (m2.find()) {
            try {
                return Integer.parseInt(m2.group(1)) * 100;
            } catch (NumberFormatException e) {
                note("parser₩", "백만원 숫자 파싱 실패: " + m2.group(1));
            }
        }
        note("parser₩", "금액(만원) 파싱 실패");
        return null;
    }

    public String joinNonNull(String... s) {
        StringBuilder sb = new StringBuilder();
        for (String v : s) if (v != null && !v.isEmpty()) sb.append(v).append('\n');
        return sb.length() == 0 ? null : sb.toString();
    }

    @SafeVarargs
    private final String joinSummaryItems(List<JsonSummaryDTO.SummaryItem>... sections) {
        StringBuilder sb = new StringBuilder();
        if (sections != null) {
            for (List<JsonSummaryDTO.SummaryItem> sec : sections) {
                if (sec == null || sec.isEmpty()) continue;
                for (JsonSummaryDTO.SummaryItem it : sec) {
                    if (it == null) continue;
                    if (it.getGroup() != null && !it.getGroup().isEmpty()) {
                        sb.append(it.getGroup()).append(": ");
                    }
                    if (it.getEvidence() != null && !it.getEvidence().isEmpty()) {
                        sb.append(it.getEvidence());
                    }
                    sb.append('\n');
                }
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public EligibilityResultDTO analyzeJson(JsonSummaryDTO summary, SelfCheckContentDto u) {
        HeuristicEvaluator h = new HeuristicEvaluator();

        Map<String, EligibilityResultDTO.EligibilityStatus> detail = new LinkedHashMap<>();

        // 1) 무주택
        var s1 = h.evalHomeless(summary, u);        detail.put("homeless", s1);

        // 2) 소득
        var s2 = h.evalIncome(summary, u);          detail.put("income", s2);

        // 3) 총자산
        var s3 = h.evalTotalAssets(summary, u);     detail.put("total_assets", s3);

        // 4) 자동차가액
        var s4 = h.evalCar(summary, u);             detail.put("car_value", s4);

        // 5) 부동산 가액
        var s5 = h.evalRealEstate(summary, u);      detail.put("real_estate_value", s5);

        // 6) 거주기간
        var s6 = h.evalResidencePeriod(summary, u); detail.put("residence_period", s6);

        // 7) 청약가입기간
        var s7 = h.evalSubscriptionPeriod(summary, u); detail.put("subscription_period", s7);

        // 8) 세대원수(문구가 있을 때만)
        var s8 = h.evalHouseholdMembers(summary, u); detail.put("household_members", s8);

        List<String> s9 = h.evalTypes(summary, u);

        // 종합 판정: 하나라도 INELIGIBLE → INELIGIBLE, 전부 ELIGIBLE/NOT_APPLICABLE → ELIGIBLE, 그 외 NEEDS_REVIEW
        EligibilityResultDTO.EligibilityStatus overall = ELIGIBLE;
        boolean anyReview = false;
        for (var st : detail.values()) {
            if (st == INELIGIBLE) { overall = INELIGIBLE; break; }
            if (st == NEEDS_REVIEW) anyReview = true;
        }
        if (overall != INELIGIBLE && anyReview) overall = NEEDS_REVIEW;

        EligibilityResultDTO out = new EligibilityResultDTO();
        out.setDetailedStatus(detail);
        out.setOverallStatus(overall);
        out.setNotes(h.getNotes());
        out.setTypes(s9);

        return out;
    }
}
