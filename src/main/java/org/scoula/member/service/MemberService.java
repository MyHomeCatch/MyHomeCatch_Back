package org.scoula.member.service;

import org.scoula.member.dto.UserInfoDto;

public interface MemberService {
    UserInfoDto findUserInfoByEmail(String email);

    void updateUserInfo(UserInfoDto userInfoDto);
}
