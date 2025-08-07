package org.scoula.house.service;

import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;

public interface ThumbService {
    public String createHousingThumb(LhHousingAttVO vo);
    public String createRentalThumb(LhRentalAttVO vo);
    public DanziAttVO createDanziThumbVO(DanziAttVO danziAttVO);
}
