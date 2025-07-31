package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.danzi.dto.DanziAttDTO;

import java.util.List;

public interface DanziAttService {
    // 단지 VO에는 rental, housing의 기존 웹 사이트 주소를 담고 create에서
    // 변환해서 저장하는 방식으로 , 그리고 저장할 때는 단지 id랑 연결된 panId 확인으로 pan_ss가 정정 공고라면 무시하도록
    // 	1.	DanziVO.단지ID -> panId → total_danzi_table에서 단지 ID 조회
    //	2.	조회된 단지 ID를 기반으로 notice_table에서 pan_ss 필드를 확인
    //	3.	pan_ss 값이 "정정공고"면 무시 (썸네일 저장하지 않음)
    //	4.	그렇지 않으면 ThumbVO로 저장

    int create(DanziAttVO danziAttVO);
    int createAll(List<DanziAttVO> danziAttVOList);
    List<DanziAttDTO> getDanziAttByDanziId(String DanziId);


}
