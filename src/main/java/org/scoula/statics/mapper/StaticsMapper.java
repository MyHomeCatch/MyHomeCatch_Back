package org.scoula.statics.mapper;

import org.apache.ibatis.annotations.Param;

import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.CompetitionRateVO;
import org.scoula.statics.domain.HousingInfoVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.dto.ScoreWinnerDTO;
import org.scoula.statics.dto.ApartmentScoreDTO;


import java.time.LocalDate;
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
    // 지역 공고 조회(모집 중 공고만) (파라미터: 지역, 오늘 날짜)
    List<HousingInfoVO> getAPTList(@Param("region") String region, @Param("date") String date);
    List<HousingInfoVO> getOfficetelList(@Param("region") String region, @Param("date") String date);
    // 공고 경쟁률 조회 (파라미터: 공고번호, | 거주지역(현재, 기타), 순위(아파트만))
    CompetitionRateVO getAPTCmpet(@Param("no") String no, @Param("reside") String reside, @Param("rank") int rank);
    CompetitionRateVO getOfficetelCmpet(@Param("no") String no);

    // 청약 가점 관련
    List<ScoreWinnerDTO> getScoreWinnersByRegion(String region);
    List<ApartmentScoreDTO> getTop5ApartmentsWithLowestScore(String region);
}
