package org.scoula.selfCheck.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.scoula.selfCheck.dto.SelfCheckRequestDto;

import java.util.List;

public interface SelfCheckMapper {

    void deleteByUserId(@Param("userId") int userId);

    void insertResult(@Param("userId") int userId, @Param("result") String result);

    List<String> findResultsByUserId(int userId);

    void insertSelfCheckContent(@Param("dto") SelfCheckRequestDto dto, @Param("userId") int userId, @Param("targetGroupsStr") String targetGroupsStr);

    void deleteSelfCheckContentByUserId(@Param("userId") int userId);

    SelfCheckContentDto findSelfCheckContentByUserId(int userId);
}
