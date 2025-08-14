package org.scoula.lh.danzi.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.dto.http.PersonalizedCardDTO;

import java.util.List;

public interface PersonalSummaryMapper {
    void upsert(PersonalizedCardDTO dto);
    PersonalizedCardDTO get(@Param("danzi_id") int danzi_id, @Param("user_id") int userId);
    List<PersonalizedCardDTO> getAllByUserId(@Param("user_id") int userId);
}
