package org.scoula.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.user.domain.User;

@Mapper
public interface AuthMapper {
    User findByEmail(String email);

    void insertUser(User user);
}
