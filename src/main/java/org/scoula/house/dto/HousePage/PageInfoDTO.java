package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfoDTO {
    private int currentPage;
    private int size;
    private int totalCount;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * 페이징 정보 생성 메서드
     */
    public static PageInfoDTO of(int currentPage, int size, int totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / size);

        return PageInfoDTO.builder()
                .currentPage(currentPage)
                .size(size)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(currentPage < totalPages - 1)
                .hasPrevious(currentPage > 0)
                .build();
    }

    /**
     * 검색 요청 DTO와 총 개수로 페이징 정보 생성
     */
    public static PageInfoDTO from(HouseSearchRequestDTO requestDto, int totalCount) {
        return of(requestDto.getPage(), requestDto.getSize(), totalCount);
    }

    /**
     * 첫 번째 페이지 여부
     */
    public boolean isFirst() {
        return currentPage == 0;
    }

    /**
     * 마지막 페이지 여부
     */
    public boolean isLast() {
        return currentPage >= totalPages - 1;
    }

    /**
     * 시작 아이템 번호 (1부터 시작)
     */
    public int getStartItem() {
        return currentPage * size + 1;
    }

    /**
     * 끝 아이템 번호
     */
    public int getEndItem() {
        int endItem = (currentPage + 1) * size;
        return Math.min(endItem, totalCount);
    }
}
