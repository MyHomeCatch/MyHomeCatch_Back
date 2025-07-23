package org.scoula.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.user.domain.User;

@Mapper
public interface AuthMapper {
    User findByEmail(String email);

    User findByNickname(String nickname);

    void insertUser(User user);

    void deleteByEmail(String email);

    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

}
