package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.dto.EligibilityResultDTO;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.scoula.lh.danzi.dto.EligibilityResultDTO.EligibilityStatus.*;
import static org.scoula.lh.danzi.service.HeuristicEvaluator.*;

@Service
public class EligibilityServiceImpl implements EligibilityService {

    // 대상 그룹의 정의와 관련 키워드는 static final로 한 번만 생성합니다.
    protected final LinkedHashMap<String, String[]> TARGET_DICT = buildTargetDict();
    public static final Map<String, Pattern> TARGET_PATTERNS = buildTargetPatterns();

    List<String> notes = new ArrayList<>();

    private void note(String category, String msg) {
        if (msg == null || msg.isEmpty()) return;
        notes.add("[" + category + "] " + msg);
    }


    private String jsonSummaryItemsToString(List<JsonSummaryDTO.SummaryItem> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.stream()
                .map(JsonSummaryDTO.SummaryItem::getEvidence)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
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
        String carText = joinNonNull(summary.getIncomeConditions(), summary.getAssetConditions(),
                summary.getApplicationRequirements(), summary.getRentalConditions());
        EligibilityResultDTO.EligibilityStatus car =
                evalCar(carText, user);
        detail.put("car", car);
        note("자동차", "판정=" + car);

        // 5) 공급 대상
        String typeText = joinNonNull(
                summary != null ? summary.getRentalConditions() : null,
                summary != null ? summary.getApplicationRequirements() : null,
                summary != null ? summary.getSelectionCriteria() : null
        );
        List<String> correspondents = evalTypes(typeText, user);
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

    private EligibilityResultDTO.EligibilityStatus calculateOverallStatus(Map<String, EligibilityResultDTO.EligibilityStatus> detail) {
        // 하나라도 부적격(INELIGIBLE)이면 전체가 부적격입니다.
        if (detail.values().stream().anyMatch(status -> status == INELIGIBLE)) {
            return INELIGIBLE;
        }
        // 부적격이 없으면서, 하나라도 검토필요(NEEDS_REVIEW)가 있으면 전체가 검토필요입니다.
        if (detail.values().stream().anyMatch(status -> status == NEEDS_REVIEW)) {
            return NEEDS_REVIEW;
        }
        // 나머지는 모두 적격입니다.
        return ELIGIBLE;
    }

    @Override
    public EligibilityResultDTO analyzeJson(JsonSummaryDTO summary, SelfCheckContentDto user) {
        notes = new ArrayList<>();
        Map<String, EligibilityResultDTO.EligibilityStatus> detail = new LinkedHashMap<>();

        String applicationRequirements = jsonSummaryItemsToString(summary.getApplicationRequirements());
        EligibilityResultDTO.EligibilityStatus homeless = evalHomeless(applicationRequirements, user);
        detail.put("homeless", homeless);

        String incomeConditions = jsonSummaryItemsToString(summary.getIncomeCriteria());
        EligibilityResultDTO.EligibilityStatus income = evalIncome(incomeConditions, user);
        detail.put("income", income);

        String assetConditions = jsonSummaryItemsToString(summary.getAssetCriteria());
        EligibilityResultDTO.EligibilityStatus asset = evalAssets(assetConditions, user);
        detail.put("total_asset", asset);

        String carText = textForAssets(summary);
        EligibilityResultDTO.EligibilityStatus car = evalCar(carText, user);
        detail.put("car_value", car);

        EligibilityResultDTO.EligibilityStatus realEstate = evalRealEstate(summary, user);
        detail.put("real_estate_value", realEstate);

        EligibilityResultDTO.EligibilityStatus residencePeriod = evalResidencePeriod(summary, user);
        detail.put("residence_period_value", residencePeriod);

        EligibilityResultDTO.EligibilityStatus subscriptionPeriod = evalSubscriptionPeriod(summary, user);
        detail.put("subscription_period_value", subscriptionPeriod);

        EligibilityResultDTO.EligibilityStatus householdMembers = evalHouseholdMembers(summary, user);
        detail.put("household_members_value", householdMembers);

        // evalTypes는 자격/부적격이 아닌, 해당하는 유형 리스트를 반환합니다.
        List<String> correspondentTypes = evalTargetGroups(summary, user);
        // 사용자가 해당하는 유형이 하나라도 있으면 '적격'으로 간주합니다.
        detail.put("types", correspondentTypes.isEmpty() ? INELIGIBLE : ELIGIBLE);


        EligibilityResultDTO result = new EligibilityResultDTO();
        result.setDetailedStatus(detail);
        result.setOverallStatus(calculateOverallStatus(detail));
        result.setTypes(correspondentTypes);
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
        note("무주택", "요건존재=" + requiresHomeless + ", 완화문구=" + relaxed +
                "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));

        if (!requiresHomeless) {
            note("무주택", "무주택 요건 비명시 → 해당 없음 처리");
            notes.add("신청 자격에 무주택 관련이 없는 것으로 보입니다."+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
            return NOT_APPLICABLE;
        }

        if (u.getIsHomeless().equals("예")) {
            note("무주택", "사용자 응답=예 (무주택)");
            notes.add("무주택 요건: 사용자가 무주택이므로 적격입니다."+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
            return ELIGIBLE;
        }
        if (relaxed) {
            note("무주택", "사용자 무주택 아님, 단 완화 문구 존재 → 추가 확인 필요"+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
            return NEEDS_REVIEW;
        } else {
            note("무주택", "사용자 무주택 아님 → 부적격"+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
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
            note("소득", "공고에서 '소득 요건 배제' 명시 → 해당 없음"+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
            return NOT_APPLICABLE;
        }

        Integer limit = extractMaxPercent(text);
        note("소득", "공고 상한% 파싱 값 " + limit  +
        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
);
        if (limit == null) return NEEDS_REVIEW;
        Integer userPct = extractMaxPercent(u.getMonthlyIncome());
        note("소득", "사용자 소득% 은 " + userPct + "로 추정돼요." +
                "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
        if (userPct == null) {
            note("소득", "사용자 퍼센트 파싱 실패 → 판정 보류: text=" +
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
            );
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
            note("자산", "공고에서 '자산 요건 배제' 명시 → 해당 없음"+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
            return NOT_APPLICABLE;
        }

        Integer limitAsset = extractFirstMoneyManwon(text);
        note("자산", "공고 총자산 상한(만원) 파싱=" + limitAsset +
        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
);
        if (limitAsset == null) {
            note("자산", "총자산 상한 파싱 실패 → 판정 보류 : text=" +
            "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
);
            return NEEDS_REVIEW;
        }
        // NEEDS_REVIEW : 자동차가액(3803)이 자산으로 들어오는 경우 파싱 실패로 간주
        if (u.getTotalAssets() != null && u.getTotalAssets().contains("3803")) {
            note("자산", "사용자 총자산에 자동차가액이 포함된 것으로 보여 파싱 실패 처리: " + u.getTotalAssets());
            return NEEDS_REVIEW;
        }
        Integer userAsset = extractFirstMoneyManwon(u.getTotalAssets());
        note("자산", "사용자 총자산(만원) 파싱=" + userAsset +
        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
);
        if (userAsset == null) {
            note("자산", "사용자 총자산 파싱 실패 → 판정 보류: text=" +
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
            );
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

    public EligibilityResultDTO.EligibilityStatus evalCar(String text, SelfCheckContentDto u) {
        note("자동차", "입력 체크: user=" + (u != null) + ", carValue=" + (u != null ? u.getCarValue() : null));
        if (text == null) {
            note("자동차", "관련 텍스트 부재 → 판정 보류");
            return NEEDS_REVIEW;
        }

        boolean mentionsCar = text.contains("자동차가액") || text.contains("차량가액") || text.contains("자동차 기준");
        note("자동차", "문구존재=" + mentionsCar+
                "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
        if (!mentionsCar) {
            note("자동차", "자동차 기준 미명시 → 해당 없음");
            return NOT_APPLICABLE;
        }
        if (text.contains("자동차가액은 3,803만원 이하") || text.contains("3,803만원 이하")) {
            note("자동차", "기준=3,803만원 이하로 탐지"+
                    "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));

            Integer userCar = extractFirstMoneyManwon(u.getCarValue());
            note("자동차", "사용자 자동차가액(만원) 파싱=" + userCar  +
            "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
);
            if (userCar == null) {
                note("자동차", "사용자 자동차가액 파싱 실패 → 판정 보류: text=" +
                        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
                );
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
        note("자동차", "자동차 문구는 있으나 구체 금액 탐지 실패 → 판정 보류: text=" +
                "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
        );
        return NEEDS_REVIEW;
    }

    public EligibilityResultDTO.EligibilityStatus evalRealEstate(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String txt = textForAssets(dto);
        boolean mentioned = containsAny(txt, "부동산", "주택가액", "공시가격", "시가표준액");
        if (!mentioned) return NOT_APPLICABLE;

        AssetLimits lim = extractAssetLimits(dto);
        Integer limit = lim.realEstateManwon;

        Integer user = parseUserRealEstate(u.getRealEstateValue());
        note("부동산", "자격 충족을 위한 한계는" + limit + "만원, 사용자의 총자산은 " + user +
        "(" + (txt != null ? txt.substring(0, Math.min(txt.length(), 80)) : "")
 + "입니다.");
        if (limit == null) {
            note("부동산", "공고 부동산 정보를 확인할 수 없어 판정 보류합니다.: text=" +
                    "(" + (txt != null ? txt.substring(0, Math.min(txt.length(), 80)) : "")
            );
            return NEEDS_REVIEW;
        }
        return (user <= limit) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityResultDTO.EligibilityStatus evalResidencePeriod(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("거주기간", "공고의 거주기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer need = extractResidenceMonths(text);
        if (need == null) return NOT_APPLICABLE;  // 점수표(배점)만 있을 가능성 → 하드요건 아님

        Integer user = u.getResidencePeriod();
        note("거주기간", "최소 " + need + " 개월의 거주기간이 필요합니다. 사용자의 거주기간은 " + user + "개월 입니다.");
        return (user >= need) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityResultDTO.EligibilityStatus evalSubscriptionPeriod(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("청약가입기간", "공고의 청약가입기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer need = extractSubscriptionMonths(text);
        if (need == null) return NOT_APPLICABLE;

        Integer user = mapUserSubPeriodToMonths(u.getSubscriptionPeriod());
        note("청약가입기간", "최소 " + need + " 개월의 가입 기간이 필요합니다. 사용자의 거주기간은 " + user + "개월 입니다.");

        return (user >= need) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityResultDTO.EligibilityStatus evalHouseholdMembers(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("세대원수", "공고의 세대원수 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        Matcher m = Pattern.compile("(\\d+)\\s*인\\s*이상").matcher(text);
        Integer need = null;
        if (m.find()) need = Integer.parseInt(m.group(1));
        if (need == null) return NOT_APPLICABLE;

        int[] parsed = parseHouseholdMembers(u.getHouseHoldMembers());

        int userMembers = parsed[0];
        note("세대원수", need + "인 이상의 세대원 수가 필요합니다. 사용자의 세대원 수는 " + userMembers + "명 입니다.");
        return (userMembers >= need) ? ELIGIBLE : INELIGIBLE;
    }

    /**
     * 공고문 텍스트를 분석하여 언급된 모든 대상 그룹을 추출합니다.
     *
     * @param noticeText 분석할 공고문 텍스트
     * @return 발견된 대상 그룹의 Set
     */
    public Set<String> detectGroupsInNotice(String noticeText) {
        if (noticeText == null || noticeText.isBlank()) {
            return Collections.emptySet();
        }
        String normalizedText = noticeText.replaceAll("\\s+", " ");

        return TARGET_PATTERNS.entrySet().stream()
                .filter(entry -> entry.getValue().matcher(normalizedText).find())
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 사용자 정보(DTO)에서 해당하는 대상 그룹을 추출합니다.
     *
     * @param user 사용자 정보 DTO
     * @return 사용자가 속한 대상 그룹의 Set
     */
    public Set<String> extractGroupsFromUser(SelfCheckContentDto user) {
        if (user == null || user.getTargetGroups() == null) {
            return Collections.emptySet();
        }
        String userGroupsString = user.getTargetGroups();
        Set<String> userGroups = new HashSet<>(); // 최종 결과를 담을 빈 Set

        if (userGroupsString != null && !userGroupsString.isBlank()) {
            // 1. 쉼표(,)를 기준으로 문자열을 자릅니다. -> ["장애인", " 신혼부부", " 다자녀"]
            String[] groupsArray = userGroupsString.split(",");

            // 2. 잘라진 각 단어의 앞뒤 공백을 제거하고 Set에 추가합니다.
            userGroups = Arrays.stream(groupsArray)
                    .map(String::trim) // 각 요소의 공백 제거 (예: " 신혼부부" -> "신혼부부")
                    .collect(Collectors.toSet());
        }
        return userGroups;
    }

    /**
     * 사용자가 입력한 텍스트(토큰)를 표준 대상 그룹 용어로 변환합니다.
     *
     * @param token 사용자가 입력한 값 (예: "장애증명")
     * @return 표준 용어 (예: "장애인")
     */
    private String canonicalizeToken(String token) {
        if (token == null || token.isBlank()) return null;
        String trimmedToken = token.trim();

        for (Map.Entry<String, Pattern> entry : TARGET_PATTERNS.entrySet()) {
            if (entry.getValue().matcher(trimmedToken).find()) {
                return entry.getKey();
            }
        }
        // 표준 용어 자체와 일치하는 경우도 확인
        return TARGET_DICT.containsKey(trimmedToken) ? trimmedToken : null;
    }

    public List<String> evalTargetGroups(JsonSummaryDTO dto, SelfCheckContentDto u) {
        // 1. 공고에서 대상 그룹과 관련된 모든 텍스트를 가져옵니다.
        String noticeText = String.valueOf(dto.getTargetGroups());
        if (noticeText == null) {
            note("대상", "공고에서 대상 그룹 정보를 찾을 수 없음");
            return Collections.emptyList();
        }

        // 2. TargetGroupMatcher를 사용하여 각각의 대상 그룹을 Set으로 추출합니다.
        Set<String> noticeGroups = detectGroupsInNotice(noticeText);
        Set<String> userGroups = extractGroupsFromUser(u);

        note("대상", "공고에서 찾은 유형: " + noticeGroups+
                "(" + (noticeText != null ? noticeText.substring(0, Math.min(noticeText.length(), 80)) : ""));
        note("대상", "사용자가 해당하는 유형: " + userGroups);

        // 3. 두 Set의 교집합을 계산합니다.
        Set<String> intersection = new HashSet<>(noticeGroups);
        intersection.retainAll(userGroups);

        if (intersection.isEmpty()) {
            note("대상", "일치하는 유형 없음");
        } else {
            note("대상", "최종 일치 유형: " + intersection);
        }

        // 4. 결과를 리스트로 변환하여 반환합니다.
        return new ArrayList<>(intersection);
    }

    public List<String> evalTypes(String text, SelfCheckContentDto u) {
        List<String> types = new ArrayList<>();
        if (text == null) {
            note("대상", "대상 추출용 텍스트 없음 → 빈 목록 반환");
            return types;
        }

        String norm = text.replaceAll("\\s+", " ");

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


        if (hits.contains("해당 없음") && hits.size() > 1) {
            hits.remove("해당 없음");
        }

        types.addAll(hits);
        note("대상", "추출 결과=" + types);
        return types;
    }

    public Integer extractMaxPercent(String text) {
        if (text == null) return null;
        Matcher m = java.util.regex.Pattern.compile("(\\d{1,3})\\s*%\\s*이하").matcher(text);
        Integer max = null;
        while (m.find()) {
            int v = Integer.parseInt(m.group(1));
            if (max == null || v > max) max = v;
        }
        if (max == null) {
            note("parser%", "퍼센트 값 파싱 실패: text=" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : ""));
        }
        return max;
    }

    public Integer extractFirstMoneyManwon(String text) {
        if (text == null) return null;
        Matcher m = java.util.regex.Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)\\s*만원\\s*이하").matcher(text);
        if (m.find()) {
            String digits = m.group(1).replace(",", "");
            try {
                return Integer.parseInt(digits);
            } catch (NumberFormatException e) {
                note("parser₩", "만원 이하 숫자 파싱 실패: text=" +
                        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
                );
            }
        }
        Matcher m2 = java.util.regex.Pattern.compile("([0-9]{2,4})\\s*백만원").matcher(text);
        if (m2.find()) {
            try {
                return Integer.parseInt(m2.group(1)) * 100;
            } catch (NumberFormatException e) {
                note("parser₩", "백만원 숫자 파싱 실패: text=" +
                        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
                );
            }
        }
        note("parser₩", "금액(만원) 파싱 실패: text=" +
                "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "")
        );
        return null;
    }

    public int[] parseHouseholdMembers(String s) {
        if (s == null) return null;
        Matcher m = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*").matcher(s);
        if (m.matches()) return new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))};
        return null;
    }

    public Integer extractResidenceMonths(String text) {
        if (text == null) return null;
        Integer min = null;
        Matcher y = Pattern.compile("(\\d{1,2})\\s*년\\s*이상").matcher(text);
        while (y.find()) {
            int months = Integer.parseInt(y.group(1)) * 12;
            if (min == null || months < min) min = months;
        }
        Matcher m = Pattern.compile("(\\d{1,3})\\s*개월\\s*이상").matcher(text);
        while (m.find()) {
            int months = Integer.parseInt(m.group(1));
            if (min == null || months < min) min = months;
        }
        note("거주기간", "거주기간 최소요건은 " + min + "개월 입니다.");
        return min;
    }

    public Integer extractSubscriptionMonths(String text) {
        if (text == null) return null;
        Matcher m = Pattern.compile("(가입기간|청약|주택청약종합저축)[^\\d]{0,15}(\\d{1,3})\\s*개월\\s*이상").matcher(text);
        Integer min = null;
        while (m.find()) {
            int months = Integer.parseInt(m.group(2));
            if (min == null || months < min) min = months;
        }
        note(
                "청약가입기간",
                "청약가입기간 최소요건은 " + min +
                        "(" + (text != null ? text.substring(0, Math.min(text.length(), 80)) : "") + ")개월 입니다."
        );
        return min;
    }

    public String joinNonNull(String... s) {
        StringBuilder sb = new StringBuilder();
        for (String v : s) if (v != null && !v.isEmpty()) sb.append(v).append("\n");
        return sb.length() == 0 ? null : sb.toString();
    }
}