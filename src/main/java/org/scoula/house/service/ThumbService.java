package org.scoula.house.service;

import org.scoula.house.dto.ThumbDTO;
import org.scoula.lh.att.domain.DanziAttVO;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;

public interface ThumbService {
    public String createHousingThumb(LhHousingAttVO vo);
    public String createRentalThumb(LhRentalAttVO vo);
    public DanziAttVO createDanziThumbVO(DanziAttVO danziAttVO);
}
