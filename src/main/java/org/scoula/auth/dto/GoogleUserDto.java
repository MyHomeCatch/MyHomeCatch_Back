package org.scoula.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserDto {
    private String id;
    private String token;
    private String email;
    private String name;
    // private String picture;
}
