package org.scoula.house.util;

import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class DateParser {

    // 한국어 날짜 패턴들
    private static final Pattern KOREAN_YEAR_MONTH_PATTERN =
            Pattern.compile("(\\d{4})년\\s*(\\d{1,2})월");
    private static final Pattern KOREAN_YEAR_MONTH_DAY_PATTERN =
            Pattern.compile("(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일");

    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        String cleanDateString = dateString.trim();

        // 1. 먼저 한국어 날짜 형식 시도
        Date koreanDate = parseKoreanDate(cleanDateString);
        if (koreanDate != null) {
            return koreanDate;
        }

        // 2. 기존 일반 날짜 형식들 시도
        String[] dateFormats = {
                "yyyyMMdd",        // 20200101
                "yyyy.MM.dd",      // 2025.06.30
                "yyyy-MM-dd",      // 2025-06-30
                "yyyy/MM/dd",      // 2025/06/30
                "yyyy.MM",         // 2025.06 (년월만)
                "yyyy-MM",         // 2025-06 (년월만)
                "yyyy/MM"          // 2025/06 (년월만)
        };

        for (String format : dateFormats) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                formatter.setLenient(false); // 엄격한 파싱 모드
                return formatter.parse(cleanDateString);
            } catch (ParseException e) {
                // 현재 형식으로 파싱 실패, 다음 형식 시도
                continue;
            }
        }

        // 파싱 실패시 로그 출력 후 null 반환
        System.err.println("Date Parser 날짜 파싱 실패: " + dateString);
        return null;
    }

    /**
     * 한국어 날짜 형식 파싱
     *
     * @param dateString 한국어 날짜 문자열
     * @return 파싱된 Date 객체 또는 null
     */
    private static Date parseKoreanDate(String dateString) {
        // "2020년 09월 15일" 형식 먼저 시도
        Matcher dayMatcher = KOREAN_YEAR_MONTH_DAY_PATTERN.matcher(dateString);
        if (dayMatcher.find()) {
            try {
                int year = Integer.parseInt(dayMatcher.group(1));
                int month = Integer.parseInt(dayMatcher.group(2)) - 1; // Calendar는 0부터 시작
                int day = Integer.parseInt(dayMatcher.group(3));

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            } catch (NumberFormatException e) {
                // 숫자 파싱 실패
            }
        }

        // "2020년 09월" 형식 시도
        Matcher monthMatcher = KOREAN_YEAR_MONTH_PATTERN.matcher(dateString);
        if (monthMatcher.find()) {
            try {
                int year = Integer.parseInt(monthMatcher.group(1));
                int month = Integer.parseInt(monthMatcher.group(2)) - 1; // Calendar는 0부터 시작

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, 1, 0, 0, 0); // 해당 월의 1일로 설정
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            } catch (NumberFormatException e) {
                // 숫자 파싱 실패
            }
        }

        return null;
    }
}