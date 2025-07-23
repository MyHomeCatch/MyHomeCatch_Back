package org.scoula.email.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDto {
    private String email;
    private int code;
}
