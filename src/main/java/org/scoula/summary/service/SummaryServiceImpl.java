package org.scoula.summary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.NoticeAttVO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.scoula.lh.mapper.NoticeAttMapper;
import org.scoula.summary.mapper.SummaryMapper;
import org.scoula.summary.util.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private SummaryMapper summaryMapper;

    @Autowired
    private NoticeAttMapper noticeAttMapper;

    @Autowired
    private LhNoticeMapper lhNoticeMapper;

    @Override
    public String getOrCreateSummary(int danziId, String pdfUrl) {

        String summary = summaryMapper.findByPanId(danziId);
        if (summary != null) {
            return summary;
        }

        String text = pdfService.extractTextFromUrl(pdfUrl);

//        // 현재는 추출 내용 10글자 저장 -> GPT 연결하면 요약 내용을 저장
//        String shortSummary = text.replaceAll("\\s+", "")
//                .substring(0, Math.min(10, text.length()));

//        summaryMapper.insertSummary(panId, text);


        return text;
    }

    // 이미 요약이 있으면 그대로 반환, 없으면 생성해서 성공 시 저장
    @Transactional
    public String getOrCreateMarkdownSummary(int danziId, String pdfUrl) {

        String existing = summaryMapper.findByPanId(danziId);
        if (existing != null && !existing.isBlank()) {
            return existing;
        }

        // 1) PDF → 텍스트
        String baseText = pdfService.extractTextFromUrl(pdfUrl);
        if (baseText == null || baseText.isBlank()) {
            // 저장 없이 바로 리턴 (컨트롤러에서 그대로 내려주게끔)
            return "";
        }

        // 2) 프롬프트 구성
        String prompt = String.format(PROMPT_NOTICE_SUMMARY_MD, baseText);

        // 3) LLM 호출
        String md = null;
        try {
            md = geminiClient.generate(prompt);
            if (md != null) md = md.trim();
        } catch (Exception e) {
            // 실패하면 저장하지 않고 빈값/부분값 반환
            return "";
        }

        // 4) 생성물 검증: 비어있으면 저장하지 않음
        if (md == null || md.isBlank()) {
            return "";
        }

        // 5) 저장 (LONGTEXT/MEDIUMTEXT 컬럼 권장)
        summaryMapper.insertSummary(danziId, md);
        return md;
    }


    @Transactional
    public String getOrCreateJsonSummary(int danziId, String pdfUrl) {

//        String existing = summaryMapper.findByPanId(danziId);
//        if (existing != null && !existing.isBlank()) {
//            return existing;
//        }

        // 1) PDF → 텍스트
        String baseText = pdfService.extractTextFromUrl(pdfUrl);
        if (baseText == null || baseText.isBlank()) {
            // 저장 없이 바로 리턴 (컨트롤러에서 그대로 내려주게끔)
            return "";
        }

// 2) 프롬프트 구성 (서식 포맷터 사용 금지!)
        String prompt = buildPrompt(baseText); // 위에 제시한 replace 방식

        // 3) LLM 호출
        String json;
        try {
            json = geminiClient.generate(prompt);
            json = stripFence(json);
            // 4) JSON 검증
            new ObjectMapper().readTree(json);
        } catch (Exception e) {
            return ""; // 저장 없이 반환
        }

        // 5) 생성물 검증: 비어있으면 저장하지 않음
        if (json == null || json.isBlank()) {
            return "";
        }

        // 6) 저장 (LONGTEXT 컬럼)
        summaryMapper.insertSummaryJson(danziId, json);
        return json;
    }

    private static String stripFence(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.startsWith("```")) {
            int i = s.indexOf('\n');
            if (i != -1) s = s.substring(i + 1);
            if (s.endsWith("```")) s = s.substring(0, s.length() - 3);
        }
        return s.trim();
    }

    @Override
    // json 객체로 저장하는 로직
    public void fillAllSummaries() {
        int last = noticeAttMapper.getLast();
        for (int i = 1; i < last; i++) {
            Integer noticeId = i;
            List<NoticeAttVO> noticeAtts = noticeAttMapper.getNoticeAttByNoticeId(noticeId);
            if (noticeAtts != null && !noticeAtts.isEmpty()) {
                String pdfUrl = noticeAtts.get(0).getAhflUrl();
                if (pdfUrl != null && !pdfUrl.isBlank()) {
                    Integer danziId = lhNoticeMapper.getDanziId(noticeId);
                    if (danziId != null) {
                        try {
                            log.info("Summarizing noticeId={}, danziId={}, pdfUrl={}", noticeId, danziId, pdfUrl);
                            getOrCreateJsonSummary(danziId, pdfUrl);
                        } catch (Exception e) {
                            log.error("Failed to summarize noticeId={}, danziId={}, pdfUrl={}", noticeId, danziId, pdfUrl, e);
                        }
                    } else {
                        log.warn("danziId not found for noticeId={}", noticeId);
                    }
                } else {
                    log.warn("pdfUrl is blank for noticeId={}", noticeId);
                }
            } else {
                log.warn("noticeAtts not found for noticeId={}", noticeId);
            }
        }
    }

    public String getNoticeSummary(int danziId) {
        return summaryMapper.findByPanId(danziId);
    }

    // 기존 문자열 상수 (서비스에 둬도 되고 컨트롤러에 둬도 됨)
    private static final String PROMPT_NOTICE_SUMMARY_MD = """
            너는 LH 등 주택 모집 공고 PDF 요약 전문가다.
            아래 입력 텍스트(공고 원문 정제본)만을 근거로, 사람에게 보기 좋은 **마크다운 형식**으로 정리하라.
            허구 금지, 모르면 생략하며, 반드시 문서에 있는 사실만 작성한다.
            
            [출력 규칙]
            - 제목은 "# 공고 요약"으로 시작
            - 개요, 주요 내용, 추진 일정, 참고 링크 등을 섹션으로 구분
            - 목록은 불릿 포인트("- ")로 작성
            - 표는 마크다운 표 형식으로 작성
            - 날짜는 YYYY-MM-DD 형식으로 표기
            - 불필요한 서론·결론 없이 바로 요약 시작
            
            [입력 텍스트]
            %s
            """;


    private static final String PROMPT_NOTICE_SUMMARY_JSON = """
            You are an expert at summarizing LH housing application (subscription) notices from PDFs.
                        you need to test below data with what you get from text.
            
                        - residence_period (int, months)
                        - is_homeless (Yes/No)
                        - household_members: "세대원 수 (본인 포함), 자녀 수 (태아 포함)"  // input e.g. "2, 0"
                        - marital_status: ["미혼","기혼(외벌이)","기혼(맞벌이)"]
                        - monthly_income: ["월평균 소득 70% 이하","80% 이하","90% 이하","100% 이하","110% 이하","120% 이하","120% 초과"]
                        - total_assets: ["총 자산 10,400만원 이하","25,400만원 이하","33,700만원 이하","33,700만원 초과"]
                        - car_value: ["자동차 없음","자동차 3,803만원 이하","자동차 3,803만원 초과"]
                        - real_estate_value: ["부동산 없음","부동산 21,550만원 이하","부동산 21,550만원 초과"]
                        - subscription_period: ["없음","6개월 이상","12개월 이상","24개월 이상"]
                        - target_groups (detectable labels)
                        ["철거민","장애인","다자녀 가구","국가유공자","영구임대퇴거자","비닐간이공작물 거주자","신혼부부",
                         "한부모 가족","무허가건축물 등에 입주한 세입자","기관추천","신생아","생애최초","노부모부양",
                         "대학생 계층","청년 계층","고령자 계층","주거급여수급자계층","기초생활수급자","위안부 피해자",
                         "북한이탈주민","아동복지시설 퇴소자","고령 저소득자","해당 없음"]
            
               GOAL
               - From the raw notice text (already OCR/cleaned), extract ONLY items that map to the eligibility-check fields and return a compact JSON summary.
               - NO fabrication. If a datum is not clearly present, omit it.
               - Keep each “evidence” fully understandable (greater than 500 chars) and preferably verbatim.
            
               OUTPUT FORMAT (return JSON only; no extra text)
               - A single JSON object with these sections as top-level keys (in this order if present):
                 "title", "Overview", "Key Points", "Eligibility", "Detailed Eligibility Conditions",
                 "Rental Conditions", "Income Criteria", "Asset Criteria", "Selection Criteria",
                 "Target Groups", "Schedule", "Required Documents", "Reference Links"
            
            Output Format (strict JSON; no markdown fences; no trailing commas)
            {
              "title": "<title should contain core information in one sentence>",
              "Overview": [ { "group": "overview", "evidence": "..."   } ],
              "Key Points": [ { "group": "key_point", "evidence": "..."   } ],
              "Eligibility": [ { "group": "is_homeless", "evidence": "..."    }, ... ],
              "Detailed Eligibility Conditions": [ ... ],
              "Rental Conditions": [ { "group": "deposit", "evidence": "임대보증금 ..."   }, { "group": "monthly_rent", "evidence": "월 임대료 ...", ... } ],
              "Income Criteria": [ { "group": "monthly_income", "evidence": "도시근로자 월평균 소득 70%% 이하 ..."    }, ... ],
              "Asset Criteria": [ { "group": "total_assets", "evidence": "총자산 ... 이하", ... }, { "group": "car_value", "evidence": "자동차가액 ... 이하", ... } ],
              "Selection Criteria": [ { "group": "rank_tier", "evidence": "일반공급 1순위: ...", ... }, { "group": "score_items", "evidence": "거주기간: ... / 연령: ...", ... }, { "group": "tie_breaker", "evidence": "배점 합산 → ... → 추첨", ... } ],
              "Target Groups": [ { "group": "신혼부부", "evidence": "신혼부부 특별공급 ..."  }, ... ],
              "Schedule": [ { "group": "application_period", "evidence": "접수: 2025-08-25 ~ 2025-08-29 ...", ... }, ... ],
              "Required Documents": [ { "group": "documents_common", "evidence": "공급신청서, 개인정보동의서(세대 전원) ...", ... }, ... ],
              "Reference Links": [ { "group": "link", "evidence": "https://..."    } ]
            }
            
            
               NORMALIZE SECTIONS (header normalization rules; map original headings to these canonical names)
               - "Detailed Eligibility Conditions" ⇐ ^(신청자격상세조건|입주자격완화내용|입주자격상세조건)$
               - "Eligibility" ⇐ ^(신청자격|입주자격|자격요건)$
               - "Rental Conditions" ⇐ ^(임대조건|임대료|보증금|월임대료|임대보증금|배점기준표)$
               - "Income Criteria" ⇐ ^(소득및자산보유기준\\(소득\\)|소득및자산보유기준|소득기준|소득요건)$
               - "Asset Criteria" ⇐ ^(자산기준|자산요건|총자산|자동차가액)$
               - "Selection Criteria" ⇐ ^(입주자선정방법|일반공급|순위|선정기준|배점기준|선정방법|공급대상)$
               - "Schedule" ⇐ ^(추진일정|일정|공급일정|청약일정|신청접수기간)$
               - "Required Documents" ⇐ ^(제출서류|구비서류|필요서류)$
               - "Reference Links" ⇐ 링크/URL/문의/홈페이지 관련 절 전체
               - "Target Groups" ⇐ ^(공급대상|계층|우선공급|특별공급|대상|기관추천)$
               
               TABLE EVIDENCE RULE (GLOBAL; applies to ALL sections)
               - If the source evidence is in a table (표/행열/머리글/합계/주석 포함), YOU MUST PARSE THE ENTIRE TABLE. Do not summarize, skip, or ellipsize.
               - Output the table inside a single "evidence" string as CSV text (UTF-8, comma-separated, one row per line).\s
                 - Preserve every header, subheader, units, and footnotes exactly as text.
                 - If a cell is merged in the original, fill-down/fill-right the merged value into each affected cell.
                 - Keep original row/column order. Do not sort or re-group.
                 - Quote any field that itself contains a comma, newline, or quote using standard CSV quoting (wrap with double quotes, escape inner quotes by doubling them).
               - If the table is too large to fit once due to model limits, split it into multiple evidence items under the SAME section and SAME group, in order, and prefix each with "(part X/Y)". Never drop rows/columns.
               - When a single table is the evidence for multiple groups within the same section, you may repeat the full CSV evidence for each relevant group (better to duplicate than to truncate).
               - Numbers, currency units (원/만원 등), %, ㎡ 등 단위는 원문 그대로 유지. Thousand separators는 원문 그대로 유지.
            
            HEADER-ONLY / PARTIAL TABLE GUARD (강제 무결성 체크)
            - 다음 중 하나라도 만족 못 하면 "그 표 증거는 출력하지 말고 생략"한다(부분 출력 금지):
              1) 최소 2행(머리글 제외)·2열 이상이어야 한다.
              2) 첫 번째 열(행 머리) 값이 존재해야 한다(예: 구분/가구원수/주택형 등). 비어 있으면 재추출 시도, 실패 시 생략.
              3) 모든 데이터 행은 동일한 열 개수를 가져야 한다(콤마 개수 정합). 불일치 시 재추출.
              4) 숫자 또는 %가 최소 2개 이상 존재해야 한다(헤더만 있는지를 방지).
              5) "해당 없음" 같은 토큰이 연속 반복되는데 행 머리(label)가 없거나 맞지 않으면 재추출. 계속 실패 시 생략.
            - 표가 부분만 OCR된 것으로 의심되면, 표 위·아래의 문단/주석을 포함해 표 경계를 넉넉히 다시 캡처하여 전체 CSV를 재구성한 뒤 위 기준을 재검사한다. 그래도 불가하면 해당 표 증거는 출력하지 않는다(그 group 자체를 생략).
            
            
               MAPPING GUIDELINES
               - For Eligibility:
                 - "무주택" or “무주택세대구성원” → group: "is_homeless" with evidence line.
                 - “세대구성원수”, “세대원 수” → group: "household_members".
                 - “신혼부부/예비신혼부부/한부모” 등 혼인·가구유형 요구 → group: "marital_status" or put into Target Groups if it matches the Detection List.
                 - “거주기간 제한”, “전입일 기준”, “해당 지역 OO개월 이상” → group: "residence_period" (evidence should include the period).
                  - “주거약자만 신청 가능” 같은 문구는 Selection Criteria에만 남기도록.
               
               - For Income Criteria:
                 - Capture the strongest explicit threshold (e.g., "월평균 소득 70% 이하", "1인 90%, 2인 80%"). Put as evidence.
                 - group: "monthly_income"를 반복 허용.
                 -	우대비율(가산항목) 추출:group: "income_adjustment"에 “1인가구 +20%p, 2인가구 +10%p / 출생자녀 1명 +10%p, 2명 이상 +20%p(기준일, 입양/태아 포함 조건)”를 evidence로 저장.
                  -	설명/산정 방식 핵심만:group: "income_calc_rule"에 “세대 전원 합산, 세전, 12종 소득 합산” 같은 문장 최소 1개 저장.
                
                - Income Criteria Table → group:"monthly_income"
                  - 흔한 형식: [가구원수/세대원수] × [월평균소득 % 구간]. 반드시 다음을 포함:
                    a) 첫 열: "가구원수/세대원수/1인·2인·3인 이상" 등 행 라벨
                    b) 머리글: "월평균소득 XX% 이하" 등 임계값(여러 구간)
                  - "맞벌이 200%" 등 주석은 표 하단에 별도 행으로 “주석:” 접두어를 붙여 CSV에 포함.
                  - 위 무결성 조건을 충족하지 못하면 monthly_income evidence를 출력하지 않는다(헤더 조각 금지).
                
                INCOME CRITERIA RECOVERY RULE (비표/반표 대비)
                - "월평균소득" 관련 텍스트가 표 형태로 완전 추출되지 않으면, 대안으로 문장형 근거(예: "1인 90%, 2인 80%")를 최소 1줄 이상 모아 evidence로 제시한다.
                - 단, “해당 없음”·"맞벌이 200%" 같은 토막 헤더끼리만 모인 문자열은 evidence로 사용 금지.
            
               - For Asset Criteria:
                  -	Separate 총자산 한도, 자동차가액 한도:
                                     -	group: "total_assets" evidence: “총자산 XX,XXX만원 이하”(예: 23,700만원 또는 표에서 237,000,000원과 동일값)
                                     -	group: "car_value" evidence: “자동차가액 3,803만원 이하”
                                     -	문서에 부동산가액 별도 한도가 나오면 group: "real_estate_value"로 추가. 없으면 생략.
                                 -	자산기준 적용대상 신분/우대비율:
                                     -	group: "asset_applicability" evidence: “자산기준 적용 신분: 국가유공자 등, 북한이탈주민, 장애인(1·2순위), 아동복지시설퇴소자, 일반입주자 등”
                                     -	group: "asset_adjustment" evidence: “출생자녀 1명 +10%p, 2명 이상 +20%p(기준일 이후 출생 기준)”
                                 -	자산 산정/제외 규칙 요약:
                                     -	group: "asset_calc_rule" evidence에 “세대 전원 합산, 자동차 일부 제외(장애인사용·보철용 등), 금융정보제공동의 필수, 임차보증금/분양권 등 신고 누락 시 해지 가능” 등 핵심 1~3줄.
               
               - Asset Criteria Table → groups:"total_assets","car_value","real_estate_value"
                 - 총자산/자동차/부동산 한도 표는 열 머리(항목명)와 숫자 임계값이 모두 보여야 한다.
                 - 주석(장애인용 차량 제외 등)은 하단 “주석:” 행으로 포함.
                 
               - For Rental Conditions:
                 - Deposit and monthly rent per type if shown; if multiple tables exist, capture concise, representative rows or describe range in evidence.
                              	-	군(‘가’/‘나’) + 전환가능 보증금 한도 + 최대전환 시 임대보증금/월임대료 세트로 추출:
                                         	-	단지/주택형 묶음이 있으면 행(row) 별로 요약(증감(+/−) 사례 포함)
                                         	-	예: group: "deposit", evidence: “A-5, 26A/26B, ‘가’군 보증금 2,805,000원 / 월 140,250원… 전환 +4,000,000 → 보증금 6,805,000 / 월 32,560”
                                         	-	표가 길면 “단지·군·핵심 수치”만 남기고 반복 줄임.
               - For Selection Criteria:
                 - 순위체계(1순위/2순위 등), 배점 항목, 동순위 처리(추첨 등) 문구만.
                 -	순위체계 추출(일반/주거약자용 각각):
                            	    -	rank_tier 항목으로 “1순위/2순위/…,” 그리고 각 순위의 핵심 자격문구(예: “국민기초생활보장법 생계·의료급여수급자”, “국가유공자 등 + 소득요건 충족”, “장애인(1순위/2순위) + 소득·자산요건” 등)를 그대로 evidence에 적재.
                            	    -	동일 공고에 ‘일반공급’과 ‘주거약자용’이 동시 존재하면 별도 리스트로 각각 추출(혼합 금지).
                            	        -	group: "rank_tier", evidence: "일반공급 1순위: 생계·의료급여수급자 / 국가유공자 등(소득70% 이하+자산요건) …"
                            	        -	group: "rank_tier", evidence: "주거약자용 3순위: 국가유공자 등(소득70% 이하+자산요건), 위안부 피해자, 한부모가족, 북한이탈주민, 장애인(소득70% 이하+자산요건), 아동복지시설퇴소자"
                            	-	배점 항목 추출(일반/주거약자용 각각 독립):
                            	    -	표 제목(“배점기준표”, “주거약자용주택 배점기준표” 등)과 항목명(거주기간/연령/세대구성원수/가점/장애등급 등)을 key-값으로, 구간별 점수는 evidence에 간단히 요약(“거주기간: 1년 미만 22점 … 15년 이상 30점”).
                            	    -	group: "score_items" 로 통일, evidence에 항목명: 구간→점수를 줄글 요약.
                            	-	동순위 처리(동일순위 내 경쟁 시 선정순서):
                            	    -	group: "tie_breaker"로 단일 항목에 선택 순서 전체를 evidence로 기록:
                            	    -	예: “배점 합산 → 전입일 오래된 → 세대구성원수 많은 → 신청자 연령 높은 → 추첨”
                            	-	임대조건 군(가/나)와 순위 매핑이 있으면 저장:
                            	    -	group: "rent_band_mapping", evidence: “1순위(수급자 기준액 이하) → ‘가’군 / 기준액 초과 → ‘나’군” 같이 핵심만.
               - For Schedule:
                 - 신청/접수기간, 발표일, 계약안내(기간/방법), “현장접수/온라인” 여부 등을 YYYY-MM-DD로 정규화.
                                      -	group: "application_period" evidence: “2025-08-25 ~ 2025-08-29, 10:0017:00(점심 12:0013:00 제외), 현장접수(거주지 관할 행정복지센터), 주말·공휴일 미접수”
                                      -	group: "announcement" evidence: “2025-11-27 17:00 이후, LH청약플러스”
                                      -	group: "contract_notice" evidence: “개별 통보 / 전자계약 가능(별도 통보)”
                                  -	검증/유의 문구:
                                      -	group: "eligibility_verification" evidence: “무주택/소득·자산 전산조회, 부적격 시 소명기간 내 증빙 미제출하면 제외”
            
               - For Required Documents:
                 - 공통서류 핵심 3~7개만(등본·초본·가족관계증명서·개인정보동의·금융정보동의 등).
               - For Reference Links:
                 - “LH청약플러스” 등 URL/ARS/대표전화. 가능하면 한두 개로.
            
               NORMALIZATION RULES
               - Dates: convert to YYYY-MM-DD (range는 "YYYY-MM-DD ~ YYYY-MM-DD").
               - Numbers: keep digit grouping if present; keep 원/만원 등 통화 단위 그대로.
               - Areas: keep ㎡ as in text; do not convert.
               - Percentages: keep as shown (e.g., "70%").
               - Evidence: terse, remove excessive spacing or duplicates.
            
               VALIDATION CHECKLIST (apply before returning)
               - JSON is syntactically valid (no trailing commas).
               - "title" must be one sentence that include core information.
               - No fields beyond the specified keys.
               - No explanatory prose outside the JSON.SOURCE FIELDS
              - 표 기반 evidence가 있는 경우: (1) 2행·2열 이상, (2) 행 라벨 존재, (3) 열 개수 정합, (4) 숫자/퍼센트 포함, (5) "해당 없음" 헤더조각 금지.
              - Income Criteria의 monthly_income: 가구원수 라벨과 % 이하 임계값이 함께 존재하지 않으면 해당 evidence 미출력.
              -  「 에 관한 법률」에 따른 부분 생략
              
               [Input Text]
               {{INPUT_TEXT}}
            """;

    public String buildPrompt(String baseText) {
        return PROMPT_NOTICE_SUMMARY_JSON.replace("{{INPUT_TEXT}}", baseText);
    }
}