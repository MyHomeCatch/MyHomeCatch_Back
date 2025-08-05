package org.scoula.lh.service;

import org.scoula.lh.domain.LhNoticeVO;
import java.util.List;

public interface LhNoticeService {
    List<LhNoticeVO> getAllLhNoticesNew();
    LhNoticeVO getLhNoticeByPanIdNew(String panId);
}
