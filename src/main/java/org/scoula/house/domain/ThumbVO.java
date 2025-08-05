package org.scoula.house.domain;

import lombok.Builder;

@Builder
public record ThumbVO(
        String panId,
        String district,
        String flDsCdNm,
        String imgPath
) {
}
