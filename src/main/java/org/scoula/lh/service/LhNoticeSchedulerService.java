package org.scoula.lh.service;

import org.scoula.lh.dto.NoticeApiDTO;
import org.scoula.lh.dto.NoticeDTO;

import java.util.List;

public interface LhNoticeSchedulerService {
    List<NoticeDTO> getNotices();
    NoticeDTO getNotice(String panId);
    int create(NoticeApiDTO notice);
    int createAll(List<NoticeApiDTO> notices);
}
