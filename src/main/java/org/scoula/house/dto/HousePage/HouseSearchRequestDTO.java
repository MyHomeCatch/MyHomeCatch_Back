package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseSearchRequestDTO {
    private String cnpCdNm;      // 지역명 (시/도 등)
    private String aisTpCdNm;    // 공고유형명 (분양/임대 등)
    private String panSs;        // 공고상태 (접수중/마감 등)

    @Min(value = 0, message = "페이지는 0 이상이어야 합니다")
    private int page = 0;

    @Min(value = 1, message = "크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "크기는 100 이하여야 합니다")
    private int size = 20;

    public int getOffset() {
        return page * size;
    }
}
