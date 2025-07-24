package org.scoula.house.util;

import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
public class DateParser {
    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        String cleanDateString = dateString.trim();

        // 지원할 날짜 형식들
        String[] dateFormats = {
                "yyyyMMdd",        // 20200101
                "yyyy.MM.dd",      // 2025.06.30
                "yyyy-MM-dd",      // 2025-06-30 (추가로 지원)
                "yyyy/MM/dd"       // 2025/06/30 (추가로 지원)
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
        System.err.println("날짜 파싱 실패: " + dateString);
        return null;
    }
}
