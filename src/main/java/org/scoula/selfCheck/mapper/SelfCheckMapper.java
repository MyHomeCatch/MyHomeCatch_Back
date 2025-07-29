package org.scoula.selfCheck.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelfCheckMapper {

    void deleteByUserId(@Param("userId") int userId);

    void insertResult(@Param("userId") int userId, @Param("result") String result);

    List<String> findResultsByUserId(int userId);

}
