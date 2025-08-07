package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.util.RegionMapper; // RegionMapper import 필요

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseSearchRequestDTO {
    private List<String> cnpCdNmList;      // 지역명 리스트 (시/도 등)
    private List<String> aisTpCdNmList;    // 공고유형명 리스트 (분양/임대 등)
    private List<String> panSsList;        // 공고상태 리스트 (접수중/마감 등)

    // 기존 단일 값도 지원 (하위 호환성)
    private String cnpCdNm;
    private String aisTpCdNm;
    private String panSs;

    @Min(value = 0, message = "페이지는 0 이상이어야 합니다")
    private int page = 0;

    @Min(value = 1, message = "크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "크기는 100 이하여야 합니다")
    private int size = 20;

    public int getOffset() {
        return page * size;
    }

    // 지역명 리스트가 비어있지 않은지 확인
    public boolean hasCnpCdNms() {
        return (cnpCdNmList != null && !cnpCdNmList.isEmpty()) ||
                (cnpCdNm != null && !cnpCdNm.trim().isEmpty());
    }

    // 공고유형명 리스트가 비어있지 않은지 확인
    public boolean hasAisTpCdNms() {
        return (aisTpCdNmList != null && !aisTpCdNmList.isEmpty()) ||
                (aisTpCdNm != null && !aisTpCdNm.trim().isEmpty());
    }

    // 공고상태 리스트가 비어있지 않은지 확인
    public boolean hasPanSs() {
        return (panSsList != null && !panSsList.isEmpty()) ||
                (panSs != null && !panSs.trim().isEmpty());
    }

    /**
     * 콤마로 구분된 문자열을 리스트로 변환하는 전처리 메소드
     */
    public void preprocessFilters() {
        // 지역명 전처리: 콤마로 구분된 문자열을 리스트로 변환
        if (cnpCdNm != null && !cnpCdNm.trim().isEmpty() && cnpCdNm.contains(",")) {
            cnpCdNmList = Arrays.stream(cnpCdNm.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            cnpCdNm = null; // 단일 값은 null로 설정
        }

        // 공고유형 전처리
        if (aisTpCdNm != null && !aisTpCdNm.trim().isEmpty() && aisTpCdNm.contains(",")) {
            aisTpCdNmList = Arrays.stream(aisTpCdNm.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            aisTpCdNm = null;
        }

        // 공고상태 전처리
        if (panSs != null && !panSs.trim().isEmpty() && panSs.contains(",")) {
            panSsList = Arrays.stream(panSs.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            panSs = null;
        }
    }

    /**
     * 지역명 매핑을 수행하는 메소드
     * 단일 지역명과 다중 지역명 모두 처리
     */
    public void mapRegionNames() {
        // 단일 지역명 매핑
        if (cnpCdNm != null && !cnpCdNm.trim().isEmpty()) {
            cnpCdNm = RegionMapper.mapToFullRegion(cnpCdNm);
        }

        // 다중 지역명 매핑
        if (cnpCdNmList != null && !cnpCdNmList.isEmpty()) {
            cnpCdNmList = cnpCdNmList.stream()
                    .filter(region -> region != null && !region.trim().isEmpty())
                    .map(RegionMapper::mapToFullRegion)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 필터 전처리와 지역 매핑을 한번에 수행
     */
    public void processFilters() {
        preprocessFilters();
        mapRegionNames();
    }
}