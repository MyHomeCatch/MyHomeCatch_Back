package org.scoula.lh.service;

import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.mapper.LhNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LhNoticeServiceImpl implements LhNoticeService {

    @Autowired
    private LhNoticeMapper lhNoticeMapper;

    @Override
    public List<LhNoticeVO> getAllLhNoticesNew() {
        return lhNoticeMapper.getAllLhNoticesNew();
    }

    @Override
    public LhNoticeVO getLhNoticeByPanIdNew(String panId) {
        return lhNoticeMapper.getLhNoticeByPanIdNew(panId);
    }

    @Override
    public List<LhNoticeVO> getPopularLhNotices() {
        return lhNoticeMapper.getPopularLhNotices();
    }
}
