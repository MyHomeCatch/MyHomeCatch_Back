package org.scoula.statics.mapper;

import org.scoula.statics.domain.WinnerRateVO;

public interface StaticsMapper {
    // 지역 코드 조회
    public int getRegionId(String region);
    // 지역 연령대별 당첨 정보 조회
    public WinnerRateVO getWinnerRate(int regionId);
}
