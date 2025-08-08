package org.scoula.lh.danzi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.domain.DanziNoticeVO;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.danzi.mapper.DanziMapper;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DanziServiceImpl implements DanziService {

    private final DanziMapper danziMapper;
    private final LhNoticeMapper lhNoticeMapper;

    @Override
    public int create(DanziVO vo) {
        return 0;
    }

    @Override
    public List<DanziVO> createAll(List<DanziVO> voList) {
        // danzi_id 매핑오류
//        int cnt = danziMapper.insert(voList);
//        if(voList.size() == cnt)
//            return voList;
//        return null; // insert된 행과 추가하려는 데이터 수 다름(에러처리)

        if (voList == null || voList.isEmpty()) {
            return null;
        }
        for (DanziVO vo : voList) {
            danziMapper.insertOne(vo);
        }
        return voList;
    }

    @Override
    public void createDanziNotice(DanziNoticeVO vo) {
        danziMapper.insertDanziNotice(vo);
    }

    @Override
    public boolean isCorrectedNoticeByDanziId(Integer danziId) {
        Integer noticeId = lhNoticeMapper.getNoticeId(danziId);
        if(noticeId == null) return false;
        LhNoticeVO vo = lhNoticeMapper.getLhNotice(String.valueOf(noticeId));
        if (vo == null) {
            log.warn("공고가 존재하지 않음. noticeId: {}", noticeId);
            return false; // 혹은 기본값 처리
        } else {
            if(vo.getPanSs().equals("정정공고중")) return true;
        }
        return false;
    }

    @Override
    public void updateMissingNoticeMapping(String panId, int danziId) {
        Integer noticeId = lhNoticeMapper.getNoticeId(danziId);
        if (noticeId != null) {
            lhNoticeMapper.updateNoticeIdByDanziId(danziId, noticeId);
            log.info("Updated notice_id={} for danzi_id={}", noticeId, danziId);
        } else {
            log.warn("Failed to find notice_id for pan_id={}, danzi_id={}", panId, danziId);
        }
    }
}
