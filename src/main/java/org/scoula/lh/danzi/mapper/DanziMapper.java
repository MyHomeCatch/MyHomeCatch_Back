package org.scoula.lh.danzi.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.lh.danzi.domain.DanziNoticeVO;
import org.scoula.lh.danzi.domain.DanziVO;
import org.scoula.lh.danzi.dto.NoticeInfoDTO;

import java.util.List;

public interface DanziMapper {
    int insert(@Param("list") List<DanziVO> list);
    void insertDanziNotice(DanziNoticeVO vo);
    DanziVO findById(int danziId);
    List<NoticeInfoDTO> findNoticesByDanziId(int danziId);
    int insertOne(DanziVO vo); // foreach가 danzi_id 매핑을 못해주는 문제 해결용
    List<Integer> findIdsInDanziApply();
    int getNoticeIdsByDanziId(int danziId);
}
