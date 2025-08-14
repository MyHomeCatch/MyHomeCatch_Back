// org/scoula/summary/parsing/MarkdownSectionExtractor.java
package org.scoula.summary.parsing;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** "## 섹션" 단위로 잘라서 원하는 제목 키워드로 가져오는 단순 파서 */
public final class MarkdownSectionExtractor {
    private static final Pattern H2_OR_H3 = Pattern.compile("(?m)^(#{2,3})[ \t]+(.+?)(?=\\s*(?:#+\\s*)?$)");
    // 추가: 굵은 불릿을 H2로 승격
    private static final Pattern BOLD_BULLET = Pattern.compile("(?m)^\\s*[-*]\\s*\\*\\*([^*]+?)\\*\\*\\s*:?.*$");


    public static String extractSection(String markdown, String... headerCandidates) {
        if (markdown == null || markdown.isEmpty()) return null;
        // 0) 전처리: 굵은 불릿을 H2로 승격
        String md = BOLD_BULLET.matcher(markdown).replaceAll("## $1");

        // 1) 섹션 인덱싱
        List<Section> sections = splitByH2_OR_H3(md);

        // 2) 제목 후보 노멀라이즈 후 매칭
        Set<String> normalizedTargets = new HashSet<>();
        for (String h : headerCandidates) {
            normalizedTargets.add(normalizeHeader(h));
        }

        for (Section s : sections) {
            String nh = normalizeHeader(s.title);
            // 완전일치 또는 양방향 포함 허용
            if (normalizedTargets.contains(nh)) return s.body.trim();
            for (String t : normalizedTargets) {
                if (nh.contains(t) || t.contains(nh)) {
                    return s.body.trim();
                }
            }
        }
        return null;
    }

    private static List<Section> splitByH2_OR_H3(String md) {
        Matcher m = H2_OR_H3.matcher(md);
        List<Section> out = new ArrayList<>();
        int lastStart = -1;
        String lastTitle = null;

        while (m.find()) {
            if (lastTitle != null) {
                out.add(new Section(lastTitle, md.substring(lastStart, m.start())));
            }
            lastTitle = m.group(2).trim();
            lastStart = m.end();
        }
        // 마지막 섹션
        if (lastTitle != null && lastStart <= md.length()) {
            out.add(new Section(lastTitle, md.substring(lastStart)));
        }
        return out;
    }

    private static String normalizeHeader(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFKC)
                .replaceAll("[\\*:：:()\\[\\]【】\\-]", "")
                .replaceAll("^[#\\d.\\-\\s]+", "") // 선행 #/번호 제거
                // 유니코드 공백까지 전부 제거 (NBSP, 전각 스페이스 포함)
                .replaceAll("[\\u00A0\\u2000-\\u200B\\u3000\\p{Z}\\s]+", "")
                .toLowerCase(Locale.KOREA);

        return t;
    }

    private static class Section {
        final String title;
        final String body;
        Section(String title, String body) { this.title = title; this.body = body; }
    }

}