package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.dto.EligibilityResultDTO;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.selfCheck.dto.SelfCheckContentDto;

public interface EligibilityService {
    public EligibilityResultDTO analyze(NoticeSummaryDTO summary, SelfCheckContentDto user);
    public EligibilityResultDTO.EligibilityStatus evalHomeless(String text, SelfCheckContentDto u);
    public EligibilityResultDTO.EligibilityStatus evalIncome(String text, SelfCheckContentDto u);
    public EligibilityResultDTO.EligibilityStatus evalAssets(String text, SelfCheckContentDto u);
    public EligibilityResultDTO.EligibilityStatus evalCar(String text, SelfCheckContentDto u);
    public Integer extractMaxPercent(String text);
    public Integer extractFirstMoneyManwon(String text);
    public String joinNonNull(String... s);
    public EligibilityResultDTO analyzeJson(JsonSummaryDTO summary, SelfCheckContentDto u);
}
