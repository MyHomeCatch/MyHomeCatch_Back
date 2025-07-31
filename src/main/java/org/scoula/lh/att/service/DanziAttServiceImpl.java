package org.scoula.lh.att.service;

import lombok.RequiredArgsConstructor;
import org.scoula.house.service.ThumbServiceImpl;
import org.scoula.lh.att.domain.DanziAttVO;
import org.scoula.lh.att.dto.DanziAttDTO;
import org.scoula.lh.att.mapper.DanziAttMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//     /**
//     * 주어진 panId가 정정공고인지 여부
//     * @param panId 공고 ID
//     * @return true: 정정공고, false: 일반공고 또는 미존재
//     */

/// /    public boolean isCorrectedNotice(String panId);
/// //            // 1. danzi_id → panId 조회
/// //            List<String> panIdList = totalDanziMapper.getPanIdsByDanziId(danziId); // 단지-공고 연결 테이블에서 조회
/// //            if (panIdList == null || panIdList.isEmpty()) return null;
/// //
/// //            for (String panId : panIdList) {
/// //                // 2. 해당 panId의 공고 정보에서 pan_ss 확인
/// //                String panSs = noticeMapper.getPanSsByPanId(panId); // 공고 테이블에서 조회
/// //
/// //                // 3. 정정공고면 스킵
/// //                if ("정정공고".equals(panSs)) {
/// //                    System.out.println("정정공고로 처리되지 않음: " + panId);
/// //                    continue;
/// //                }

@Service
@RequiredArgsConstructor
public class DanziAttServiceImpl implements DanziAttService {

    private final ThumbServiceImpl thumbService;
    //private final NotticeService
    private final DanziAttMapper mapper;

    @Override
    public List<DanziAttDTO> getDanziAttByDanziId(String DanziId) {
        return List.of();
    }

    @Override
    public int createAll(List<DanziAttVO> danziAttVOList) {
        List<DanziAttVO> returnVO = new ArrayList<>();

        for(DanziAttVO vo : danziAttVOList) {
            // 정정공고 스킵
            DanziAttVO forSaveVO= thumbService.createDanziThumbVO(vo);
            mapper.create(forSaveVO);
            returnVO.add(forSaveVO);
        }
        return returnVO.size();
    }

    @Override
    public int create(DanziAttVO danziAttVO) {
        // 정정공고 스킵
        DanziAttVO forSaveVO= thumbService.createDanziThumbVO(danziAttVO);
        return mapper.create(forSaveVO);
    }
}
