package org.scoula.statics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantRegionVO {
    private Long id;
    private Long regionId;
    private String statDe;
    private int age30;
    private int age40;
    private int age50;
    private int age60;
}
