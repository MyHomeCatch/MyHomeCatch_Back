package org.scoula.chapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.chapi.domain.ApplyHomePublicPrivateRentVO;

@Mapper
public interface PublicPrivateRentDBMapper {
    // 공공지원민간임대
    int insert(ApplyHomePublicPrivateRentVO vo);
}
