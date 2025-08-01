package org.scoula.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String name;
    private String nickname;
    private String email;
    private String address;
    private int additionalPoint;
    private String currentPassword;
    private String newPassword;

}
