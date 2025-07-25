package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.domain.NoticeAttVO;
import org.scoula.lh.domain.housing.LhHousingApplyVO;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.housing.LhHousingVO;
import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingHouseVO {
    private Integer lhHousingId;
    private String panId;
    private String bzdtNm;
    private String lctAraAdr;
    private String lctAraDtlAdr;
    private String minMaxRsdnDdoAr;
    private String sumTotHshCnt;
    private String htnFmlaDsCdNm;
    private String mvinXpcYm;

    private LhNoticeVO notice;
    private List<NoticeAttVO> noticeAttList;
    private List<LhHousingApplyVO> applyList;
    private List<LhHousingAttVO> housingAttList;
}
