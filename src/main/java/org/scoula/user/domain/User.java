package org.scoula.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
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
