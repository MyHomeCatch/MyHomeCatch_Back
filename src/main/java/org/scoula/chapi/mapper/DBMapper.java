package org.scoula.chapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.chapi.domain.CHOfficetelModelVO;
import org.scoula.chapi.domain.CHOfficetelVO;

@Mapper
public interface DBMapper {
    int insert(CHOfficetelVO vo);

    int insertOfficetelModel(CHOfficetelModelVO vo);
}

