package org.scoula.applyHome.mapper;

import org.scoula.applyHome.domain.ApplyHomeVO;
import org.scoula.applyHome.dto.ApplyHomeDTO;

import java.util.List;

public interface ApplyHomeMapper {
    int create(ApplyHomeDTO dto);
    List<ApplyHomeVO> getList();
    ApplyHomeVO get(String NOTICE_ID);
}
