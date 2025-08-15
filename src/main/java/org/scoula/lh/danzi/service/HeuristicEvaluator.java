package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.scoula.lh.danzi.dto.EligibilityResultDTO.EligibilityStatus;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.scoula.lh.danzi.dto.EligibilityResultDTO.EligibilityStatus.*;

public class HeuristicEvaluator {
    public static final List<String> notes = new ArrayList<>();
    static public void note(String tag, String msg) { if (msg != null && !msg.isEmpty()) notes.add("[" + tag + "] " + msg); }
    public List<String> getNotes() { return notes; }

    public static final LinkedHashMap<String, String[]> TARGET_DICT = buildTargetDict();
    public static Map<String, Pattern> TARGET_PATTERNS = buildTargetPatterns();


   public static LinkedHashMap<String, String[]> buildTargetDict() {
        LinkedHashMap<String, String[]> dict = new LinkedHashMap<>();
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
        return dict;
    }

    public static Map<String, Pattern> buildTargetPatterns() {
        Map<String, Pattern> map = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : TARGET_DICT.entrySet()) {
            String patternString = Arrays.stream(entry.getValue())
                    .filter(kw -> kw != null && !kw.isEmpty())
                    .map(kw -> Pattern.quote(kw.trim()))
                    .collect(Collectors.joining("|"));

            if (!patternString.isEmpty()) {
                map.put(entry.getKey(), Pattern.compile(patternString, Pattern.CASE_INSENSITIVE));
            }
        }
        return map;
    }

    /* ========= Normalization ========= */

    /** json 텍스트(문자열) → 공백 정리/코드펜스/주석 제거 */
    public static String normalize(String s) {
        if (s == null) return null;
        if (s.startsWith("\uFEFF")) s = s.substring(1);              // BOM
        s = s.replaceAll("(?s)^\\s*```(?:json)?\\s*", "");            // ```json
        s = s.replaceAll("(?s)```\\s*$", "");                         // ```
        s = s.replaceAll("(?s)<!--.*?-->", "");                       // <!-- -->
        s = s.replaceAll("(?m)^\\s*//.*$", "");                       // //
        s = s.replaceAll("(?s)/\\*.*?\\*/", "");                      // /* */
        s = s.replace("\\n", "\n");                                   // literal \n → LF
        s = s.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "");   // control
        s = s.trim();
        return s;
    }

    /** DTO 전체를 평탄화 (백업용) */
    public static String normalize(JsonSummaryDTO dto) {
        if (dto == null) return null;
        StringBuilder sb = new StringBuilder();
        append(sb, dto.getTitle());
        addItems(sb, dto.getOverview());
        addItems(sb, dto.getKeyPoints());
        addItems(sb, dto.getApplicationRequirements()); // Eligibility + Detailed Eligibility
        addItems(sb, dto.getRentalConditions());
        addItems(sb, dto.getIncomeCriteria());
        addItems(sb, dto.getAssetCriteria());
        addItems(sb, dto.getSelectionCriteria());
        addItems(sb, dto.getSchedule());
        addItems(sb, dto.getRequiredDocuments());
        addItems(sb, dto.getReferenceLinks());
        addItems(sb, dto.getTargetGroups());
        return normalize(sb.toString());
    }

    public static void addItems(StringBuilder sb, List<JsonSummaryDTO.SummaryItem> list) {
        if (list == null) return;
        for (JsonSummaryDTO.SummaryItem it : list) {
            append(sb, it.getGroup());
            append(sb, it.getEvidence());
        }
    }
    public static void append(StringBuilder sb, String s) { if (s != null && !s.isEmpty()) sb.append(s).append('\n'); }

    /* ========= CSV parser (robust, RFC4180-ish) ========= */

    /** 간단하지만 견고한 CSV 파서 (큰따옴표, 이스케이프, 줄바꿈 지원) */
    public static List<String[]> parseCsv(String csv) {
        List<String[]> rows = new ArrayList<>();
        if (csv == null) return rows;

        int i = 0, n = csv.length();
        List<String> row = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuote = false;

        while (i < n) {
            char c = csv.charAt(i++);
            if (inQuote) {
                if (c == '"') {
                    if (i < n && csv.charAt(i) == '"') { field.append('"'); i++; } // "" → "
                    else inQuote = false;
                } else {
                    field.append(c);
                }
            } else {
                if (c == '"') inQuote = true;
                else if (c == ',') { row.add(field.toString()); field.setLength(0); }
                else if (c == '\r') { /* skip */ }
                else if (c == '\n') { row.add(field.toString()); field.setLength(0); rows.add(row.toArray(new String[0])); row.clear(); }
                else field.append(c);
            }
        }
        // last field
        if (inQuote) { /* 비정상 CSV → 강제 닫기 */ }
        row.add(field.toString());
        rows.add(row.toArray(new String[0]));

        // 공백/양 끝 트림
        for (int r = 0; r < rows.size(); r++) {
            String[] arr = rows.get(r);
            for (int c = 0; c < arr.length; c++) {
                arr[c] = arr[c] == null ? "" : arr[c].trim();
            }
        }
        // 빈 마지막 행 제거
        while (!rows.isEmpty()) {
            boolean allEmpty = true;
            for (String s : rows.get(rows.size() - 1)) if (!s.isEmpty()) { allEmpty = false; break; }
            if (allEmpty) rows.remove(rows.size() - 1); else break;
        }
        return rows;
    }

    public static boolean looksLikeCsvTable(String s) {
        if (s == null) return false;
        // 콤마+개행이 모두 있고, 최소 2행 2열 이상
        if (!(s.contains(",") && s.contains("\n"))) return false;
        List<String[]> rows = parseCsv(s);
        if (rows.size() < 2) return false;
        int cols0 = rows.get(0).length;
        if (cols0 < 2) return false;
        int numericCells = 0;
        for (int r = 1; r < rows.size(); r++) {
            String[] arr = rows.get(r);
            if (arr.length != cols0) return false; // 열 정합성
            for (String cell : arr) if (cell.matches(".*(\\d|%|원|만원).*")) { numericCells++; break; }
        }
        return numericCells >= 2;
    }

    /* ========= Number / Money parsing ========= */

    public static Integer safeInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return null; } }

    /** "23,700만원", "( 23,700 )만원", "237,000,000(원)" 등 → 만원 단위 정수 */
    public static Integer parseMoneyToManwon(String s) {
        if (s == null) return null;
        String t = s.replaceAll("[()\\s]", "");
        // 1) 12,345만원
        Matcher m1 = Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)만원").matcher(t);
        if (m1.find()) return Integer.parseInt(m1.group(1).replace(",", ""));

        // 2) N원 (예: 261,000,000) → 만원
        Matcher m2 = Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)원").matcher(t);
        if (m2.find()) {
            long won = Long.parseLong(m2.group(1).replace(",", ""));
            return (int) Math.round(won / 10000.0);
        }

        // 3) 순수 숫자만 있는 경우: 자산 표에서 "261,000,000" 등
        if (t.matches("[0-9]{1,3}(?:,[0-9]{3})+")) {
            long won = Long.parseLong(t.replace(",", ""));
            // 1000단위 이상이면 원으로 보고 만원 변환
            if (won >= 100_000) return (int) Math.round(won / 10000.0);
        }

        // 4) "n억 n천만원"
        Matcher m3 = Pattern.compile("(d+)억\\s*(d{1,3})?천?\\s*만원?").matcher(s);
        if (m3.find()) {
            int eok = Integer.parseInt(m3.group(1));
            int cheon = m3.group(2) != null ? Integer.parseInt(m3.group(2)) : 0;
            return eok * 10000 + cheon * 1000;
        }

        // 5) "n천만원"
        Matcher m4 = Pattern.compile("(d{1,3})천\\s*만원?").matcher(s);
        if (m4.find()) return Integer.parseInt(m4.group(1)) * 1000;

        return null;
    }

    public static Integer parsePercent(String s) {
        if (s == null) return null;
        Matcher m = Pattern.compile("(\\d{1,3})\\s*%").matcher(s);
        if (m.find()) return Integer.parseInt(m.group(1));
        return null;
    }

    /* ========= Section-focused text builders ========= */

    public static String joinEvidence(List<JsonSummaryDTO.SummaryItem> items) {
        if (items == null) return null;
        StringBuilder sb = new StringBuilder();
        for (var it : items) append(sb, it.getEvidence());
        return normalize(sb.toString());
    }

    public static String textForEligibility(JsonSummaryDTO dto) {
        StringBuilder sb = new StringBuilder();
        append(sb, joinEvidence(dto.getApplicationRequirements()));
        // 보조: Overview/Key Points에도 자격 요건이 섞이는 경우가 있어 추가
        append(sb, joinEvidence(dto.getOverview()));
        return normalize(sb.toString());
    }

    public static String textForIncome(JsonSummaryDTO dto) {
        return normalize(joinEvidence(dto.getIncomeCriteria()));
    }

    public static String textForAssets(JsonSummaryDTO dto) {
        return normalize(joinEvidence(dto.getAssetCriteria()));
    }

    /* ========= High-precision extractors using section CSV ========= */

    /** Income Criteria 전용: 표가 있으면 그 표에서 최솟값(보수적) % 추출, 없으면 문장에서 "OO% 이하" 최솟값 */
    public Integer extractIncomeLimitPercent(JsonSummaryDTO dto) {
        String txt = textForIncome(dto);
        if (txt == null) return null;

        // CSV 우선
        Integer best = null;
        for (String block : txt.split("\\n{2,}")) {
            if (!looksLikeCsvTable(block)) continue;
            List<String[]> rows = parseCsv(block);
            int cols = rows.get(0).length;

            // 헤더에서 소득 관련 열 인덱스 탐색
            Map<String, Integer> headerIndex = new HashMap<>();
            for (int c = 0; c < cols; c++) {
                String h = rows.get(0)[c];
                if (h.contains("소득기준") || h.contains("최종 소득") || h.matches(".*소득.*%.*")) headerIndex.put("income", c);
            }

            if (headerIndex.isEmpty()) {
                // 열 이름이 명확치 않으면 전체 셀에서 % 스캔
                for (int r = 1; r < rows.size(); r++) {
                    for (String cell : rows.get(r)) {
                        Integer p = parsePercent(cell);
                        if (p != null) best = (best == null || p < best) ? p : best;
                    }
                }
            } else {
                int ic = headerIndex.get("income");
                for (int r = 1; r < rows.size(); r++) {
                    String cell = rows.get(r)[ic];
                    Integer p = parsePercent(cell);
                    if (p != null) best = (best == null || p < best) ? p : best;
                }
            }
        }

        // 텍스트 보조
        if (best == null) {
            Matcher m = Pattern.compile("(\\d{1,3})\\s*%\\s*(?:이하|이내)").matcher(txt);
            while (m.find()) {
                int v = Integer.parseInt(m.group(1));
                best = (best == null || v < best) ? v : best;
            }
        }
        note("소득",  best + "이하에 해당해야 합니다.");
        return best;
    }

    /** Asset Criteria 전용: CSV에서 '총자산가액/자동차가액/부동산' 열을 찾아 최솟값(보수적, 만원) */
    public static class AssetLimits { Integer totalManwon; Integer carManwon; Integer realEstateManwon; }
    public static AssetLimits extractAssetLimits(JsonSummaryDTO dto) {
        String txt = textForAssets(dto);
        AssetLimits out = new AssetLimits();

        // 1) CSV evidence
        if (txt != null) {
            for (String block : txt.split("\\n{2,}")) {
                if (!looksLikeCsvTable(block)) continue;
                List<String[]> rows = parseCsv(block);
                String[] header = rows.get(0);
                int totalIdx = -1, carIdx = -1, reIdx = -1;

                for (int c = 0; c < header.length; c++) {
                    String h = header[c];
                    if (h.contains("총자산")) totalIdx = c;
                    else if (h.contains("자동차")) carIdx = c;
                    else if (h.contains("부동산")) reIdx = c;
                }

                for (int r = 1; r < rows.size(); r++) {
                    if (totalIdx >= 0) {
                        Integer v = parseMoneyToManwon(rows.get(r)[totalIdx]);
                        if (v != null) out.totalManwon = minNN(out.totalManwon, v);
                    }
                    if (carIdx >= 0) {
                        Integer v = parseMoneyToManwon(rows.get(r)[carIdx]);
                        if (v != null) out.carManwon = minNN(out.carManwon, v);
                    }
                    if (reIdx >= 0) {
                        Integer v = parseMoneyToManwon(rows.get(r)[reIdx]);
                        if (v != null) out.realEstateManwon = minNN(out.realEstateManwon, v);
                    }
                }
            }
        }

        // 2) 텍스트 보조 (예: "( 23,700 )만원 이하", "3,803만원 이하")
        String all = (txt != null ? txt : "") + "\n" + normalize(dto != null ? dto.getTitle() : "");
        // 총자산
        if (out.totalManwon == null) {
            String target = sliceNear(all, "(총자산|자산\\s*기준|자산가액|금융자산|일반자산)", 200);
            if (target != null) {
                Integer v = extractFirstMoneyManwon(target);
                if (v != null) out.totalManwon = v;
            }
        }
        // 자동차
        if (out.carManwon == null) {
            String target = sliceNear(all, "(자동차가액|차량가액|자동차\\s*기준)", 200);
            if (target != null) {
                Integer v = extractFirstMoneyManwon(target);
                if (v != null) out.carManwon = v;
            }
        }
        // 부동산
        if (out.realEstateManwon == null) {
            String target = sliceNear(all, "(부동산|주택가액|공시가격|시가표준액)", 200);
            if (target != null) {
                Integer v = extractFirstMoneyManwon(target);
                if (v != null) out.realEstateManwon = v;
            }
        }

        note("자산", "총 자산=" + out.totalManwon + "만원, 자동차=" + out.carManwon + "만원, 부동산=" + out.realEstateManwon + "만원");
        return out;
    }

    /* ========= Public helpers reused below ========= */

    /** 텍스트에서 'XX% 이하/이내' 최솟값 */
    public Integer extractSmallestPercentLimit(String text) {
        if (text == null) return null;
        Matcher m = Pattern.compile("(\\d{1,3})\\s*%\\s*(?:이하|이내)").matcher(text);
        Integer min = null;
        while (m.find()) {
            int v = Integer.parseInt(m.group(1));
            if (min == null || v < min) min = v;
        }
        note("소득", "보수적으로 판단하여 " + min + "이하가 안전합니다.");
        return min;
    }

    /** 텍스트에서 금액(만원) 1개 추출 (여러 포맷 지원) */
    public static Integer extractFirstMoneyManwon(String text) {
        if (text == null) return null;
        // 괄호 포함 변종 우선 처리
        Matcher m0 = Pattern.compile("\\(\\s*([0-9]{1,3}(?:,[0-9]{3})*)\\s*\\)\\s*만원").matcher(text);
        if (m0.find()) return Integer.parseInt(m0.group(1).replace(",", ""));

        // 표준 패턴들
        Integer v;
        // 1) 12,345만원
        Matcher m1 = Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)\\s*만원\\s*(?:이하|이내)?").matcher(text);
        if (m1.find()) { v = Integer.parseInt(m1.group(1).replace(",", "")); return v; }
        // 2) n억 n천만원
        Matcher m2 = Pattern.compile("(\\d+)\\s*억\\s*(\\d{1,3})?\\s*천?\\s*만?원?\\s*(?:이하|이내)?").matcher(text);
        if (m2.find()) { int eok = Integer.parseInt(m2.group(1)); int cheon = m2.group(2) != null ? Integer.parseInt(m2.group(2)) : 0; return eok*10000 + cheon*1000; }
        // 3) n천만원
        Matcher m3 = Pattern.compile("(\\d{1,3})\\s*천\\s*만?원?\\s*(?:이하|이내)?").matcher(text);
        if (m3.find()) { return Integer.parseInt(m3.group(1)) * 1000; }
        // 4) 261,000,000원
        Matcher m4 = Pattern.compile("([0-9]{1,3}(?:,[0-9]{3})*)\\s*원\\s*(?:이하|이내)?").matcher(text);
        if (m4.find()) { long won = Long.parseLong(m4.group(1).replace(",", "")); return (int)Math.round(won/10000.0); }
        // 5) 순수 숫자만
        Matcher m5 = Pattern.compile("\\b([0-9]{1,3}(?:,[0-9]{3})+)\\b").matcher(text);
        if (m5.find()) { long won = Long.parseLong(m5.group(1).replace(",", "")); if (won >= 100_000) return (int)Math.round(won/10000.0); }
        note("금액 판단 수치", "금액(만원) 한계 판단 근거가 정확하지 않으므로 공고문을 직접 확인하시길 바랍니다.");
        return null;
    }

    public static Integer minNN(Integer a, Integer b) { return (a == null) ? b : (b == null ? a : Math.min(a, b)); }

    public static boolean containsAny(String text, String... kws) {
        if (text == null) return false;
        for (String kw : kws) if (kw != null && !kw.isEmpty() && text.contains(kw)) return true;
        return false;
    }

    public static String sliceNear(String text, String keyRegex, int radius) {
        if (text == null) return null;
        Matcher m = Pattern.compile(keyRegex).matcher(text);
        if (m.find()) {
            int c = m.start();
            int from = Math.max(0, c - radius);
            int to = Math.min(text.length(), c + radius);
            return text.substring(from, to);
        }
        return null;
    }

    /* ========= User input parsers ========= */

    public Integer parseUserIncomePercent(String s) {
        if (s == null) return null;
        Matcher m = Pattern.compile("(\\d{1,3})\\s*%").matcher(s);
        if (!m.find()) return null;
        int v = Integer.parseInt(m.group(1));
        if (s.contains("초과")) return 999;
        return v;
    }

    public static Integer parseUserTotalAssets(String s) {
        if (s == null) return null;
        Integer v = extractFirstMoneyManwon(s + " 이하");
        if (v != null) return v;
        if (s.contains("초과")) return 999_999_999;
        return null;
    }

    public static Integer parseUserRealEstate(String s) {
        if (s == null) return null;
        if (s.contains("없음")) return 0;
        Integer v = extractFirstMoneyManwon(s + " 이하");
        if (v != null) return v;
        if (s.contains("초과")) return 999_999_999;
        return null;
    }

    public Integer parseUserCar(String s) {
        if (s == null) return null;
        if (s.contains("없음")) return 0;
        Integer v = extractFirstMoneyManwon(s + " 이하");
        if (v != null) return v;
        if (s.contains("초과")) return 999_999_999;
        return null;
    }

    public int[] parseHouseholdMembers(String s) {
        if (s == null) return null;
        Matcher m = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*").matcher(s);
        if (m.matches()) return new int[]{ Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)) };
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
        note("청약가입기간", "청약가입기간 최소요건은 " + min + "개월 입니다.");
        return min;
    }


    public Set<String> extractUserTargetGroups(SelfCheckContentDto u) {
        LinkedHashSet<String> out = new LinkedHashSet<>();
        if (u == null) return out;

        Object raw = null;
        try { // getTargetGroups()
            var m = u.getClass().getMethod("getTargetGroups");
            raw = m.invoke(u);
        } catch (Exception ignore) {}
        if (raw == null) {
            try { // getTypes()
                var m = u.getClass().getMethod("getTypes");
                raw = m.invoke(u);
            } catch (Exception ignore) {}
        }

        List<String> toks = new ArrayList<>();
        if (raw instanceof Collection) {
            for (Object o : (Collection<?>) raw) if (o != null) toks.add(o.toString());
        } else if (raw instanceof String) {
            String s = ((String) raw).trim();
            if (!s.isEmpty()) {
                // 콤마/세미콜론/개행 구분
                toks.addAll(Arrays.asList(s.split("[,;\\n]+")));
            }
        }

        // 토큰 → 표준 키로 정규화
        for (String t : toks) {
            String canon = canonicalizeTargetGroupToken(t);
            if (canon != null) out.add(canon);
        }
        // 추가로 원문 토큰이 표준 키 그 자체면 그대로 인정
        for (String t : toks) {
            String tt = t.trim();
            if (TARGET_DICT.containsKey(tt)) out.add(tt);
        }

        // 로그
        for (String h : out) note("사용자 해당 대상", " '" + h + "' ");
        return out;
    }

    public String canonicalizeTargetGroupToken(String token) {
        if (token == null || token.isBlank()) return null;
        String s = token.trim();
        for (Map.Entry<String, Pattern> e : TARGET_PATTERNS.entrySet()) {
            if (e.getValue().matcher(s).find()) return e.getKey();
        }
        return null;
    }

    /* ========= Evaluators (DTO-aware, high precision) ========= */

    public Set<String> detectTargetGroupsInNotice(JsonSummaryDTO dto) {
        LinkedHashSet<String> hits = new LinkedHashSet<>();
        StringBuilder sb = new StringBuilder();
        // 1순위: Target Groups 섹션
        if (dto.getTargetGroups() != null) {
            for (var it : dto.getTargetGroups()) append(sb, it.getEvidence());
        }
        // 보조: Eligibility/Selection에 묻어있는 경우
        if (sb.length() == 0) {
            append(sb, joinEvidence(dto.getApplicationRequirements()));
            append(sb, joinEvidence(dto.getSelectionCriteria()));
        }
        String norm = normalize(sb.toString());
        if (norm == null || norm.isEmpty()) return hits;

        for (Map.Entry<String, Pattern> e : TARGET_PATTERNS.entrySet()) {
            if (e.getValue().matcher(norm).find()) {
                hits.add(e.getKey());
                note("대상(공고)", "에서 언급된 대상 목록: '" + e.getKey() + "' ");
            }
        }
        return hits;
    }

    // 섹션별 evidence 사용
    public EligibilityStatus evalHomeless(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        note("무주택", "사용자 입력값 =" + (u != null ? u.getIsHomeless() : null));
        if (text == null) {
            note("무주택", "공고의 무주택 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }

        boolean requires = containsAny(text, "무주택세대구성원", "무주택 세대구성원", "세대 전원 무주택", "무주택");
        boolean relaxed  = containsAny(text, "무주택요건 완화", "소형·저가주택", "무주택 간주");
        if (!requires) {return NOT_APPLICABLE;}
        if (u == null || u.getIsHomeless() == null) {
            note("무주택", "사용자의 무주택 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        if ("예".equals(u.getIsHomeless())) return ELIGIBLE;
        return relaxed ? NEEDS_REVIEW : INELIGIBLE;
    }

    public EligibilityStatus evalIncome(JsonSummaryDTO dto, SelfCheckContentDto u) {
        if (u == null || u.getMonthlyIncome() == null) {
            note("소득", "사용자의 소득 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        Integer limit = extractIncomeLimitPercent(dto); // 섹션 표 기반
        if (limit == null) {
            // 백업: 전체 텍스트에서라도
            limit = extractSmallestPercentLimit(normalize(dto));
        }
        Integer user = parseUserIncomePercent(u.getMonthlyIncome());
        note("소득", "소득 자격 충족을 위한 한계는 " + limit + "이고, 사용자의 소득은 " + user+ "입니다.");
        if (limit == null || user == null) {
            note("소득", "공고 사용자 소득 정보를 확인할 수 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        return (user <= limit) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalTotalAssets(JsonSummaryDTO dto, SelfCheckContentDto u) {
        if (u == null || u.getTotalAssets() == null) {
            note("총자산", "사용자의 총자산 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        AssetLimits lim = extractAssetLimits(dto);
        Integer limit = lim.totalManwon; // 보수적으로 최솟값 사용
        Integer user  = parseUserTotalAssets(u.getTotalAssets());
        note("총자산", "총자산 자격 충족을 위한 한계는" + limit + "만원, 사용자의 총자산은 "  + user+"입니다.");
        if (limit == null || user == null) {
            note("총자산", "공고 총자산 정보를 확인할 수 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        return (user <= limit) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalCar(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String txt = textForAssets(dto);
        boolean mentioned = containsAny(txt, "자동차가액", "차량가액", "자동차 기준", "자동차 가액");
        if (!mentioned) return NOT_APPLICABLE;

        AssetLimits lim = extractAssetLimits(dto);
        Integer limit = lim.carManwon;
        if (limit == null && containsAny(txt, "3,803만원")) limit = 3803; // fallback
        if (u == null || u.getCarValue() == null) {
            note("자동차", "사용자의 자동차 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer user = parseUserCar(u.getCarValue());
        note("자동차", "자격 충족을 위한 한계는" + limit + "만원, 사용자의 총자산은 "  + user+"입니다.");
        if (limit == null || user == null) {
            note("자동차", "공고 자동차 정보를 확인할 수 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        return (user <= limit) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalRealEstate(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String txt = textForAssets(dto);
        boolean mentioned = containsAny(txt, "부동산", "주택가액", "공시가격", "시가표준액");
        if (!mentioned) return NOT_APPLICABLE;

        AssetLimits lim = extractAssetLimits(dto);
        Integer limit = lim.realEstateManwon;
        if (u == null || u.getRealEstateValue() == null) {
            note("부동산", "사용자의 부동산 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer user = parseUserRealEstate(u.getRealEstateValue());
        note("부동산", "자격 충족을 위한 한계는" + limit + "만원, 사용자의 총자산은 "  + user+"입니다.");
        if (limit == null || user == null) {
            note("부동산", "공고 또는 사용자 부동산 정보를 확인할 수 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        return (user <= limit) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalResidencePeriod(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("거주기간", "공고의 거주기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer need = extractResidenceMonths(text);
        if (need == null) return NOT_APPLICABLE;  // 점수표(배점)만 있을 가능성 → 하드요건 아님
        if (u == null || u.getResidencePeriod() == 0) {
            note("거주기간", "사용자의 거주기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer user = u.getResidencePeriod();
        note("거주기간", "최소 " + need + " 개월의 거주기간이 필요합니다. 사용자의 거주기간은 " + user + "개월 입니다.");
        return (user >= need) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalSubscriptionPeriod(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("청약가입기간", "공고의 청약가입기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer need = extractSubscriptionMonths(text);
        if (need == null) return NOT_APPLICABLE;
        if (u == null || u.getSubscriptionPeriod() == null) {
            note("청약가입기간", "사용자의 청약가입기간 관련 항목이 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        Integer user = mapUserSubPeriodToMonths(u.getSubscriptionPeriod());
        note("청약가입기간", "최소 " + need + " 개월의 가입 기간이 필요합니다. 사용자의 거주기간은 " + user + "개월 입니다.");
        if (user == null) {
            note("청약가입기간", "사용자의 청약가입기간 정보를 확인할 수 없어 판정 보류합니다.");
            return NEEDS_REVIEW;
        }
        return (user >= need) ? ELIGIBLE : INELIGIBLE;
    }

    public EligibilityStatus evalHouseholdMembers(JsonSummaryDTO dto, SelfCheckContentDto u) {
        String text = textForEligibility(dto);
        if (text == null) {
            note("세대원수", "공고의 세대원수 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        Matcher m = Pattern.compile("(\\d+)\\s*인\\s*이상").matcher(text);
        Integer need = null;
        if (m.find()) need = Integer.parseInt(m.group(1));
        if (need == null) return NOT_APPLICABLE;
        if (u == null || u.getHouseHoldMembers() == null) {
            note("세대원수", "사용자의 세대원수 관련 항목이 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        int[] parsed = parseHouseholdMembers(u.getHouseHoldMembers());
        if (parsed == null || parsed.length < 1) {
            note("세대원수", "사용자의 세대원수 정보를 확인할 수 없어 판정 보류");
            return NEEDS_REVIEW;
        }
        int userMembers = parsed[0];
        note("세대원수",  need + "인 이상의 세대원 수가 필요합니다. 사용자의 세대원 수는 " + userMembers + "명 입니다.");
        return (userMembers >= need) ? ELIGIBLE : INELIGIBLE;
    }

    public List<String> evalTypes(JsonSummaryDTO dto, SelfCheckContentDto u) {
        Set<String> notice = detectTargetGroupsInNotice(dto);
        Set<String> user = extractUserTargetGroups(u);

        LinkedHashSet<String> types = new LinkedHashSet<>();
        for (String canon : TARGET_DICT.keySet()) {
            if (notice.contains(canon) && user.contains(canon)) {
                types.add(canon);
                note("대상", "공고에 해당하는 사용자의 유형은'" + canon + "' 입니다.");
            }
        }
        List<String> result = new ArrayList<>(types);

        return result;
    }

    /* ========= User convenience ========= */

    public static Integer mapUserSubPeriodToMonths(String s) {
        if (s == null) return null;
        if (s.contains("없음")) return 0;
        if (s.contains("24")) return 24;
        if (s.contains("12")) return 12;
        if (s.contains("6"))  return 6;
        return null;
    }
}
