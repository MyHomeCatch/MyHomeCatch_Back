package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HousePageResponseDTO {
    private List<HouseCardDTO> housingList;
    private PageInfoDTO pageInfo;
}
