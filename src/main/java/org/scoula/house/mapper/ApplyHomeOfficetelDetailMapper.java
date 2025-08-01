package org.scoula.house.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.house.domain.ApplyHomeOfficetelHouseVO;
import org.scoula.house.dto.ApplyHomeOfficetelDetailDTO;

import java.util.List;

@Mapper
public interface ApplyHomeOfficetelDetailMapper {
    // GET Methods
//    List<ApplyHomeOfficetelDetailDTO> get(String pblancNo);

    ApplyHomeOfficetelHouseVO get(String pblancNo);
}
