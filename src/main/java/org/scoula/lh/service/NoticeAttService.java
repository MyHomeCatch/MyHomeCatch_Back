package org.scoula.lh.service;

import org.scoula.lh.danzi.domain.NoticeAttVO;
import org.scoula.lh.dto.NoticeAttDTO;

import java.util.List;

public interface NoticeAttService {
    int create(NoticeAttVO noticeAttVO);
    int createAll(List<NoticeAttVO> noticeAttVOList);
    List<NoticeAttDTO> getNoticeAttByPanId(String panId);
}
