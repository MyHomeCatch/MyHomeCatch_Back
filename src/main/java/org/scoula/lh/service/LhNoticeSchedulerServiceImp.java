package org.scoula.lh.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.dto.NoticeApiDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class LhNoticeSchedulerServiceImp implements LhNoticeSchedulerService {

    private final LhNoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNotices() {
        return noticeMapper.getLhNotices().stream().map(NoticeDTO::of).toList();
    }

    @Override
    public NoticeDTO getNotice(String panId) {
        return NoticeDTO.of(noticeMapper.getLhNotice(panId));
    }


    public int create(NoticeApiDTO noticeApiDTO) {
        int row = noticeMapper.create(noticeApiDTO.toVO());
        return row;
    }

    public int createAll(List<NoticeApiDTO> noticeApiDTO) {
        int row = noticeMapper.createAll(noticeApiDTO.stream().map(NoticeApiDTO::toVO).toList());
        return row;
    }

    @Override
    public List<NoticeDTO> createAllAndReturnNew(List<NoticeApiDTO> allNotices) {
        List<NoticeDTO> newNotices = new ArrayList<>();

        for (NoticeApiDTO apiNotice : allNotices) {
            if (!noticeMapper.existsByPanId(apiNotice.getPanId())) {
                int result = create(apiNotice);
                if (result > 0) {
                    NoticeDTO newNotice = getNotice(apiNotice.getPanId());
                    newNotices.add(newNotice);
                }
            } else if(!noticeMapper.getLhNotice(apiNotice.getPanId()).getPanNm().equals(apiNotice.getPanNm())) {
                int result = create(apiNotice);
                if (result > 0) {
                    NoticeDTO newNotice = getNotice(apiNotice.getPanId());
                    newNotices.add(newNotice);
                }
            }
        }

        return newNotices;
    }

    @Override
    public boolean existsByPanId(String panId) {
        return noticeMapper.existsByPanId(panId);
    }
}
