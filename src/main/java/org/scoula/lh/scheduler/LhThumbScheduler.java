package org.scoula.lh.scheduler;

import lombok.RequiredArgsConstructor;
import org.scoula.house.mapper.ThumbMapper;
import org.scoula.house.service.ThumbService;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.mapper.lhHousing.LhHousingAttMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class LhThumbScheduler {

    private final LhHousingAttMapper housingAttMapper;
    private final ThumbService thumbService;

    public void processAllHousingThumbnails(){
        List<LhHousingAttVO> allHousings = housingAttMapper.getAll();

        System.out.println("allHousings size: " + allHousings.size());

        for (LhHousingAttVO housing : allHousings) {
            thumbService.createThumb(housing);
        }
    }
}
