package org.scoula.lh.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.dto.NoticeApiDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        int row = noticeMapper.create(noticeApiDTO.toLHNoticeVO());
        return row;
    }

    public int createAll(List<NoticeApiDTO> noticeApiDTO) {
        int row = noticeMapper.createAll(noticeApiDTO.stream().map(NoticeApiDTO::toLHNoticeVO).toList());
        return row;
    }

    @Override
    public List<NoticeDTO> createAllAndReturnNew(List<NoticeApiDTO> allNotices) {
        List<NoticeDTO> newNotices = new ArrayList<>();

        // 1. 현재 공고 테이블 panId 조회
        List<String> curPanIdList = noticeMapper.getPanIds();

        // 2. 현재 테이블에 존재하지 않는 NoticeApiDTO 수집
        Set<String> curPanIdSet = new HashSet<>(curPanIdList);

        List<NoticeApiDTO> newAddNotices = allNotices.stream()
                .filter(dto -> !curPanIdSet.contains(dto.getPanId()))
                .collect(Collectors.toList());

        if(newAddNotices.size() == 0) return newNotices; // 추가된 데이터가 없으면 빈 리스트 반환

        // 3. 데이터 insert
        int cnt = createAll(newAddNotices);

        // 4. 추가된 데이터 반환하기
        // 4-1. 추가한 데이터 panId List

        List<String> addPanIdList = newAddNotices.stream()
                .map(NoticeApiDTO::getPanId)
                .collect(Collectors.toList());

        newNotices = noticeMapper.getLHNoticeList(addPanIdList).stream()
                .map(vo -> NoticeDTO.of(vo))
                .collect(Collectors.toList());

        return newNotices;
    }


    @Override
    public boolean existsByPanId(String panId) {
        return noticeMapper.existsByPanId(panId);
    }

    @Override
    public void createNoticeDanzi(Integer noticeId, Integer danziId) {
        noticeMapper.createNoticeDanzi(noticeId, danziId);
    }

    @Override
    public int getDanziId(Integer noticeId) {
        return noticeMapper.getDanziId(noticeId);
    }
}
