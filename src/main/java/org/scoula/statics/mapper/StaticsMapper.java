package org.scoula.statics.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.statics.domain.*;

import java.util.List;

public interface StaticsMapper {
    // 지역 코드 조회
    int getRegionId(String region);
    // 공공데이터 DB 저장
    void insertApplicantList(@Param("list") List<ApplicantRegionVO> list);
    void insertWinnerList(@Param("list") List<WinnerRegionVO> list);
    // 지역 연령대별 당첨 정보 조회
    // 1. 지역 연령대별 신청 정보 조회
    ApplicantRegionVO getApplicant(@Param("regionId") long regionId, @Param("date") String date);
    // 2. 지역 연령대별 당첨 정보 조회
    WinnerRegionVO getWinner(@Param("regionId") long regionId, @Param("date") String date);
    // 지역 공고 조회(모집 중 공고만)
    List<HousingInfoVO> getAPTList();
    List<HousingInfoVO> getOfficetelList();
    // 공고 경쟁률 조회
    CompetitionRateVO getAPTCmpet();
    CompetitionRateVO getOfficetelCmpet();

}
