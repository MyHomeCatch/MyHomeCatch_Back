package org.scoula.statics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinnerInfoVO {
    private Long id;
    private Long regionId;
    private int medianScoreRegion;
    private int avgScoreRegion;
    private int lowestScoreRegion;
    private int highestScoreRegion;
}