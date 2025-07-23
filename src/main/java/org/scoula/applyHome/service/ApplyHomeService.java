package org.scoula.applyHome.service;

import org.scoula.applyHome.dto.ApplyHomeAnalysisDTO;
import org.scoula.applyHome.dto.ApplyHomeDTO;
import org.scoula.applyHome.dto.ApplyHomeSpecialDTO;

import java.util.List;

public interface ApplyHomeService {
    int create(ApplyHomeDTO dto);
    int createAnalysis(ApplyHomeAnalysisDTO dto);
    int createSpecial(ApplyHomeSpecialDTO dto);
//    List<ApplyHomeDTO> getApplyHomeList();
//    ApplyHomeDTO getApplyHome(String NOTICE_ID);
}
