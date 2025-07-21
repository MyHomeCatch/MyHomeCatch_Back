package org.scoula.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginInfoDto {
    private String id;
    private String token;
    private String nickname;
    private String profile;
    private String email;
    private String birthday;
}
