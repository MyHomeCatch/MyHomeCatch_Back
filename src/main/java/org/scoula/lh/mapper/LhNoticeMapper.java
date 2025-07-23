package org.scoula.lh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.lh.domain.LhNoticeVO;

import java.util.List;

@Mapper
public interface LhNoticeMapper {
    LhNoticeVO getLhNotice(String panId);
    List<LhNoticeVO> getLhNotices();
    int create(LhNoticeVO lhNoticeVO);
    int createAll(List<LhNoticeVO> lhNoticeVOList);
}
