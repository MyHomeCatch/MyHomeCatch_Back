package org.scoula.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalPointDto {
    private String email;
    private int additionalPoint;
}
