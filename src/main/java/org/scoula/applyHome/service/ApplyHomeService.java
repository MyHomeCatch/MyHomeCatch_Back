package org.scoula.applyHome.service;

import org.scoula.applyHome.dto.ApplyHomeDTO;

import java.util.List;

public interface ApplyHomeService {
    int create(ApplyHomeDTO dto);
    List<ApplyHomeDTO> getApplyHomeList();
    ApplyHomeDTO getApplyHome(String NOTICE_ID);
}
