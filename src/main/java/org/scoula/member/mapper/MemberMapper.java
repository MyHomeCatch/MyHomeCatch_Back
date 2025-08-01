package org.scoula.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.member.dto.AdditionalPointDto;
import org.scoula.member.dto.UserInfoDto;

@Mapper
public interface MemberMapper {
    int updateAdditionalPoint(AdditionalPointDto additionalPointDto);
}
