package org.scoula.selfCheck.mapper;

import org.apache.ibatis.annotations.Param;

public interface SelfCheckMapper {

    void deleteByUserId(@Param("userId") int userId);

    void insertResult(@Param("userId") int userId, @Param("result") String result);

}
