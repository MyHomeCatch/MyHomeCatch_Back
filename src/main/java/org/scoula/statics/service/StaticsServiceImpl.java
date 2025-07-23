package org.scoula.statics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.CompetitionRateVO;
import org.scoula.statics.domain.HousingInfoVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.dto.LowCompetitionRateDTO;
import org.scoula.statics.dto.RegionAgeDTO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final StaticsMapper mapper;

    @Override
    public RegionAgeDTO getRegionAge(String region, String date) {
        long regionId = mapper.getRegionId(region);
        ApplicantRegionVO applicantVo = mapper.getApplicant(regionId, date);
        WinnerRegionVO winnerVo = mapper.getWinner(regionId, date);
        return RegionAgeDTO.of(applicantVo, winnerVo);
    }

    @Override
    public List<LowCompetitionRateDTO> getLowCmpetRate(String region, String reside, int rank) {
        List<LowCompetitionRateDTO> lowCmpetList = new ArrayList<>();
        // 현재 날짜
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);

        // 모집중인 공고 조회
        List<HousingInfoVO> housingList = mapper.getAPTList(region, date);
        housingList.addAll(mapper.getOfficetelList(region, date));

        // 공고 경쟁률 조회
        for(HousingInfoVO vo : housingList) {
            CompetitionRateVO cmpetVo;
            if(vo.getTable_code() == 1) {
                cmpetVo = mapper.getAPTCmpet(vo.getPBLANC_NO(), reside, rank);
            } else {
                cmpetVo = mapper.getOfficetelCmpet(vo.getPBLANC_NO());
            }

            lowCmpetList.add(LowCompetitionRateDTO.of(cmpetVo, vo));
        }

        // 리스트 정렬 경쟁률 순(5개)
        lowCmpetList.sort(Comparator.comparingDouble(o ->
                (double) o.getREQ_CNT() / o.getSUPLY_HSHLDCO()
        ));
        return lowCmpetList;
    }
}
