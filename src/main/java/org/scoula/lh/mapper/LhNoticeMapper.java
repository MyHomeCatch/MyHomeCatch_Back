package org.scoula.lh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.domain.NoticeVO;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.dto.NoticeDTO;

import java.util.List;

@Mapper
public interface LhNoticeMapper {
    LhNoticeVO getLhNotice(String panId);
    List<LhNoticeVO> getLhNotices();
    int create(LhNoticeVO lhNoticeVO);
    int createAll(List<LhNoticeVO> lhNoticeVOList);
    boolean existsByPanId(String panId);
    void createNoticeDanzi(@Param("noticeId") Integer noticeId,@Param("danziId") Integer danziId);
    int getDanziId(@Param("noticeId") Integer noticeId);
    List<String> getPanIds();
    List<NoticeVO> getLHNoticeList(@Param("list") List<String> list);

}
