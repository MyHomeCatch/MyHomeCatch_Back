package org.scoula.summary.parsing;
import java.util.*;

public final class ParsingRules {
    private ParsingRules() {}

    private static List<String> list(String... a) {
        return Arrays.asList(a);
    }

    public static final Map<String, Rule> BY_KEY;
    // 기존 서비스 코드 호환: parsingRules.get("is_homeless") 형태 지원
    public static final Map<String, Rule> parsingRules;

    static {
        Map<String, Rule> m = new LinkedHashMap<>();

        // residence_period
        m.put("residence_period", new Rule(
                list("최대 거주 기간","거주기간","거주의무","임대기간","입주기간"),
                list("최대 거주 기간","거주기간","임대기간"),
                list("년","영구임대"),
                null, null, null,
                null
        ));

        // is_homeless
        m.put("is_homeless", new Rule(
                list("신청 자격","입주자격","입주자격 완화"),
                list("무주택","주택 보유 요건"),
                null,
                list("무주택세대구성원","세대 전원 무주택"),
                list("무주택요건 완화","소형·저가주택.*무주택 간주","해당지역.*주택이 없으면"),
                list("주택 소유.*불문","유주택자 가능"),
                null
        ));

        // household_members
        m.put("household_members", new Rule(
                list("선정 기준","배점 기준","신청 자격"),
                list("세대구성원","세대원","부양가족","미성년 자녀수","자녀"),
                null, null, null, null, null
        ));

        // marital_status
        m.put("marital_status", new Rule(
                list("신청 자격","공급 대상","계층"),
                null,
                list("신혼부부","예비신혼부부","한부모","배우자","기혼","미혼"),
                null, null, null, null
        ));

        // monthly_income
        m.put("monthly_income", new Rule(
                list("소득 및 자산보유 기준","신청 자격","입주자격 완화"),
                list("소득","월평균 소득","소득 요건"),
                list("% 이하","소득 배제","소득 완화"),
                null, null, null, null
        ));

        // total_assets
        m.put("total_assets", new Rule(
                list("소득 및 자산보유 기준","입주자격 완화"),
                list("총자산","자산 보유 기준","자산 요건"),
                list("만원 이하","원 이하","자산 배제"),
                null, null, null, null
        ));

        // car_value
        m.put("car_value", new Rule(
                list("소득 및 자산보유 기준","입주자격 완화","신청 자격"),
                list("자동차 가액","차량가액","자동차가액","자동차 기준"),
                list("3,803만원"),
                null, null, null, null
        ));

        // real_estate_value
        m.put("real_estate_value", new Rule(
                list("소득 및 자산보유 기준","신청 자격"),
                list("부동산","주택가액","공시가격","시가표준액"),
                list("만원 이하","소형·저가주택","1억원","1억6천만원"),
                null, null, null, null
        ));

        // subscription_period
        m.put("subscription_period", new Rule(
                list("선정 기준","배점 기준","신청 자격"),
                list("청약저축","주택청약종합저축","가입기간","납입횟수","순위"),
                list("개월","회","1순위","2순위"),
                null, null, null, null
        ));

        // target_groups
        m.put("target_groups", new Rule(
                list("공급 대상","계층","신청 자격"),
                list("공급 대상","대상","계층","우선공급","자격"),
                null, null, null, null,
                list(
                        "대학생","취업준비생","청년","사회초년생","신혼부부","예비신혼부부","한부모가족","고령자",
                        "주거급여수급자","국가유공자","장애인","주거약자","다자녀","노부모부양","생애최초","신생아",
                        "기관추천","철거민","영구임대퇴거자","비닐간이공작물 거주자","무허가건축물 세입자","북한이탈주민",
                        "아동복지시설 퇴소자","위안부 피해자","고령 저소득자","창업인"
                )
        ));

        BY_KEY = Collections.unmodifiableMap(m);
        parsingRules = BY_KEY; // 호환용 별칭
    }

    public static Rule get(String key) {
        return BY_KEY.get(key);
    }

    public static Rule get(ParsingRuleKey key) {
        return BY_KEY.get(key.key());
    }
}

