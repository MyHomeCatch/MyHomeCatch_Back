package org.scoula.applyHome.mapper;

import org.scoula.applyHome.domain.ApplyHomeAnalysisVO;
import org.scoula.applyHome.domain.ApplyHomeSpecialVO;
import org.scoula.applyHome.domain.ApplyHomeVO;


public interface ApplyHomeMapper {
    int create(ApplyHomeVO vo);
    int createAnalysis(ApplyHomeAnalysisVO vo);
    int createSpecial(ApplyHomeSpecialVO vo);
}
