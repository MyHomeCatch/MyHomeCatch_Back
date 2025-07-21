package org.scoula.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.user.domain.User;

@Mapper
public interface AuthMapper {
    User findByEmail(String email);

    User findByNickname(String nickname);

    void insertUser(User user);

    void deleteByEmail(String email);

}
