package org.scoula.lh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.domain.NoticeVO;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.dto.NoticeDTO;

import java.util.List;

@Mapper
public interface LhNoticeMapper {
    LhNoticeVO getLhNotice(@Param("panId") String panId);
    List<LhNoticeVO> getLhNotices();
    int create(LhNoticeVO lhNoticeVO);
    int createAll(List<LhNoticeVO> lhNoticeVOList);
    boolean existsByPanId(String panId);
    void createNoticeDanzi(@Param("noticeId") Integer noticeId,@Param("danziId") Integer danziId);
    int getDanziId(@Param("noticeId") Integer noticeId);
    int getNoticeId(@Param("danziId") Integer danziId);
    List<String> getPanIds();
    List<NoticeVO> getLHNoticeList(@Param("list") List<String> list);
    void updateNoticeIdByDanziId(@Param("danziId") int danziId, @Param("noticeId") int noticeId);

    // 새로운 기능 추가
    List<LhNoticeVO> getAllLhNoticesNew();
    LhNoticeVO getLhNoticeByPanIdNew(String panId);
}