package org.scoula.house.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 신청일시 문자열 파서
 * 예: "2019.03.06 15:00 ~ 2020.03.04 18:00"
 */
public class DateRangeParser {

    // 날짜 범위 추출 정규표현식 (순수 날짜 문자열용)
    private static final Pattern DATE_RANGE_PATTERN =
            Pattern.compile("(\\d{4}\\.\\d{2}\\.\\d{2}\\s+\\d{2}:\\d{2})\\s*~\\s*(\\d{4}\\.\\d{2}\\.\\d{2}\\s+\\d{2}:\\d{2})");

    // 날짜 포맷 (구버전 호환)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    // 날짜 포맷 (Java 8+)
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    /**
     * 신청일시 문자열에서 Date 배열 추출
     * @param dateRangeStr 신청일시 문자열 (예: "2019.03.06 15:00 ~ 2020.03.04 18:00")
     * @return Date[2] 배열 (Date[0]: 시작일시, Date[1]: 종료일시)
     */
    public static Date[] parseToDateArray(String dateRangeStr) {
        if (dateRangeStr == null || dateRangeStr.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = DATE_RANGE_PATTERN.matcher(dateRangeStr);
        if (!matcher.find()) {
            return null;
        }

        try {
            String startDateStr = matcher.group(1);
            String endDateStr = matcher.group(2);

            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);

            return new Date[]{startDate, endDate};
        } catch (ParseException e) {
            throw new RuntimeException("DateRangeParser 날짜 파싱 실패: " + dateRangeStr, e);
        }
    }

    /**
     * 시작일시만 추출
     * @param dateRangeStr 신청일시 문자열
     * @return 시작일시 Date
     */
    public static Date parseStartDate(String dateRangeStr) {
        Date[] dates = parseToDateArray(dateRangeStr);
        return dates != null ? dates[0] : null;
    }

    /**
     * 종료일시만 추출
     * @param dateRangeStr 신청일시 문자열
     * @return 종료일시 Date
     */
    public static Date parseEndDate(String dateRangeStr) {
        Date[] dates = parseToDateArray(dateRangeStr);
        return dates != null ? dates[1] : null;
    }
}