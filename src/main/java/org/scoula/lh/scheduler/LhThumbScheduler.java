package org.scoula.lh.scheduler;

import lombok.RequiredArgsConstructor;
import org.scoula.house.mapper.ThumbMapper;
import org.scoula.house.service.ThumbService;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;
import org.scoula.lh.mapper.lhHousing.LhHousingAttMapper;
import org.scoula.lh.mapper.lhRental.LhRentalAttMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class LhThumbScheduler {

    private final LhHousingAttMapper housingAttMapper;
    private final LhRentalAttMapper rentalAttMapper;
    private final ThumbService thumbService;

    public void processAllHousingThumbnails(){
//        List<LhHousingAttVO> allHousings = housingAttMapper.getAll();
        List<LhRentalAttVO> allRentals = rentalAttMapper.getAll();


//        for (LhHousingAttVO housing : allHousings) {
//            thumbService.createThumb(housing);
//        }
//        System.out.println("allHousings size: " + allHousings.size());

        for (LhRentalAttVO rental : allRentals) {
            thumbService.createRentalThumb(rental);
        }

        System.out.println("allRentals size: " + allRentals.size());
    }
}
