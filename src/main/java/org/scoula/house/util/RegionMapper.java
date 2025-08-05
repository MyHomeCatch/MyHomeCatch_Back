package org.scoula.house.util;

import java.util.HashMap;
import java.util.Map;

public class RegionMapper {

    // 지역명 매핑 Map (정적 초기화)
    private static final Map<String, String> REGION_MAP = new HashMap<>();

    static {
        // 광역시/특별시
        REGION_MAP.put("서울특별시", "서울");
        REGION_MAP.put("부산광역시", "부산");
        REGION_MAP.put("대구광역시", "대구");
        REGION_MAP.put("인천광역시", "인천");
        REGION_MAP.put("광주광역시", "광주");
        REGION_MAP.put("대전광역시", "대전");
        REGION_MAP.put("울산광역시", "울산");

        // 특별자치시/도
        REGION_MAP.put("세종특별자치시", "세종");
        REGION_MAP.put("강원특별자치도", "강원");
        REGION_MAP.put("전북특별자치도", "전북");
        REGION_MAP.put("제주특별자치도", "제주");

        // 도
        REGION_MAP.put("경기도", "경기");
        REGION_MAP.put("경상남도", "경남");
        REGION_MAP.put("경상북도", "경북");
        REGION_MAP.put("전라남도", "전남");
        REGION_MAP.put("충청남도", "충남");
        REGION_MAP.put("충청북도", "충북");

        // 기타
        REGION_MAP.put("전국", "전국");
        REGION_MAP.put("대구광역시 외", "대구");  // 특수 케이스
    }

    /**
     * 긴 지역명을 2글자 지역명으로 변환
     *
     * @param fullRegionName 전체 지역명
     * @return 2글자 지역명, 매핑되지 않으면 원본 반환
     */
    public static String mapToShortRegion(String fullRegionName) {
        if (fullRegionName == null || fullRegionName.trim().isEmpty()) {
            return "";
        }

        String trimmedName = fullRegionName.trim();
        return REGION_MAP.getOrDefault(trimmedName, trimmedName);
    }

    /**
     * 2글자 지역명에서 전체 지역명으로 역변환 (첫 번째 매치 반환)
     * @param shortRegionName 2글자 지역명
     * @return 전체 지역명, 매핑되지 않으면 원본 반환
     */
    public static String mapToFullRegion(String shortRegionName) {
        if (shortRegionName == null || shortRegionName.trim().isEmpty()) {
            return "";
        }

        String trimmedName = shortRegionName.trim();

        // 역방향 검색
        for (Map.Entry<String, String> entry : REGION_MAP.entrySet()) {
            if (entry.getValue().equals(trimmedName)) {
                return entry.getKey();
            }
        }

        return trimmedName; // 매핑되지 않으면 원본 반환
    }
}
