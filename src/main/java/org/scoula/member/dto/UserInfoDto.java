package org.scoula.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private int id;
    private String name;
    private String nickname;
    private String email;
    private String address;
    private int additionalPoint;
    private String currentPassword;
    private String newPassword;
    private String additionalPointUpdatedAt;

}
