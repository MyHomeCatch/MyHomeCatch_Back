package org.scoula.lh.service;

import org.scoula.lh.dto.NoticeApiDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LhNoticeSchedulerService {
    List<NoticeDTO> getNotices();
    NoticeDTO getNotice(String panId);
    int create(NoticeApiDTO notice);
    int createAll(List<NoticeApiDTO> notices);
    List<NoticeDTO> createAllAndReturnNew(List<NoticeApiDTO> allNotices);
    boolean existsByPanId(String panId);
}
