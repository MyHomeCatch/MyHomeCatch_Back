package org.scoula.summary.util;

public class TextUtils {

    public static String cleanPdfText(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return "";
        }

        // 1. 줄바꿈, 탭 제거 → 공백으로 치환
        String noLineBreaks = rawText.replaceAll("[\\n\\r\\t]+", " ");

        // 2. 여러 공백 → 하나의 공백으로
        String singleSpaced = noLineBreaks.replaceAll("\\s{2,}", " ");

        // 3. 앞뒤 공백 제거
        return singleSpaced.trim();
    }
}
