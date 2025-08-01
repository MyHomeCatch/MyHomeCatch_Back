package org.scoula.lh.mapper;

import org.scoula.lh.danzi.domain.NoticeAttVO;

import java.util.List;

public interface NoticeAttMapper {
    // table lh_notice_att -> notice_att로 변경
    // noticeAttVO panId -> notice_id로 변경
    int create(NoticeAttVO noticeAttVO);
    int createAll(List<NoticeAttVO> noticeAttVOList);
    List<NoticeAttVO> getNoticeAttByNoticeId(String noticeId);
}
