package org.scoula.statics.mapper;

import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.WinnerRateVO;
import org.scoula.statics.domain.WinnerRegionVO;

public interface StaticsMapper {
    // 지역 코드 조회
    int getRegionId(String region);
    // 공공데이터 DB 저장
    void insertApplicantRegion(ApplicantRegionVO applicant);
    void insertWinnerRegion(WinnerRegionVO winner);
    // 지역 연령대별 당첨 정보 조회
    // 1. 지역 연령대별 신청 정보 조회
    ApplicantRegionVO getApplicant(long regionId, String date);
    // 2. 지역 연령대별 당첨 정보 조회
    WinnerRegionVO getWinner(long regionId, String date);
}
