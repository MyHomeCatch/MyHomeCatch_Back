package org.scoula.lh.att.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanziAttVO {
    int danziId;
    String flDsCdNm;
    String cmnAhflNm;
    String ahflUrl;
}
