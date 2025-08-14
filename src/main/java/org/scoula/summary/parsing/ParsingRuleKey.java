package org.scoula.summary.parsing;

public enum ParsingRuleKey {
    RESIDENCE_PERIOD("residence_period"),
    IS_HOMELESS("is_homeless"),
    HOUSEHOLD_MEMBERS("household_members"),
    MARITAL_STATUS("marital_status"),
    MONTHLY_INCOME("monthly_income"),
    TOTAL_ASSETS("total_assets"),
    CAR_VALUE("car_value"),
    REAL_ESTATE_VALUE("real_estate_value"),
    SUBSCRIPTION_PERIOD("subscription_period"),
    TARGET_GROUPS("target_groups");

    private final String key;
    ParsingRuleKey(String key) { this.key = key; }
    public String key() { return key; }
}
