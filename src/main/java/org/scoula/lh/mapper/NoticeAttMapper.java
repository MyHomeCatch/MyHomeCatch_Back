package org.scoula.lh.mapper;

import org.scoula.lh.danzi.domain.NoticeAttVO;

import java.util.List;

public interface NoticeAttMapper {
    int create(NoticeAttVO noticeAttVO);
    int createAll(List<NoticeAttVO> noticeAttVOList);
    List<NoticeAttVO> getNoticeAttByPanId(String panId);
}
