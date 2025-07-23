package org.scoula.lh.service;

import lombok.RequiredArgsConstructor;
import org.scoula.lh.domain.NoticeAttVO;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.mapper.NoticeAttMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeAttServiceImp implements NoticeAttService {

    private final NoticeAttMapper noticeAttMapper;

    @Override
    public int create(NoticeAttVO noticeAttVO) {
        return noticeAttMapper.create(noticeAttVO);
    }

    @Override
    public int createAll(List<NoticeAttVO> noticeAttVOList) {
        return noticeAttMapper.createAll(noticeAttVOList);
    }

    @Override
    public List<NoticeAttDTO> getNoticeAttByPanId(String panId) {
        return noticeAttMapper.getNoticeAttByPanId(panId).stream().map(NoticeAttDTO::of).toList();
    }
}
