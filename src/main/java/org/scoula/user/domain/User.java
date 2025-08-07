package org.scoula.user.domain;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    private int userId;
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String address;
    private int additionalPoint;
    private String additionalPointUpdatedAt;
}
