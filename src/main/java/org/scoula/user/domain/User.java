package org.scoula.user.domain;

import lombok.*;

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
}
