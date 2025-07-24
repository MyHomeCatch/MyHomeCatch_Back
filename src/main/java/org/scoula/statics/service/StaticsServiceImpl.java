package org.scoula.statics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.CompetitionRateVO;
import org.scoula.statics.domain.HousingInfoVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.dto.LowCompetitionRateDTO;
import org.scoula.statics.dto.RegionAgeDTO;
import org.scoula.statics.mapper.StaticsMapper;

import org.scoula.config.ApiConfig;
import org.scoula.statics.dto.ApartmentScoreDTO;
import org.scoula.statics.dto.ScoreWinnerDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


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
    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
  
    private List<JsonNode> fetchDataFromApi(String apiPath, int page, int perPage, String conditionKey, String conditionValue) {
        try {
            // UriComponentsBuilder 로 URL 구성
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl() + apiPath)
                    .queryParam("page", page)
                    .queryParam("perPage", perPage)
                    .queryParam("returnType", "JSON");

            // 조건 파라미터가 있으면 추가
            if (conditionKey != null && conditionValue != null) {
                builder.queryParam(conditionKey, conditionValue);
            }

            // serviceKey는 별도 덧붙임 (인코딩 문제 방지)
            String urlWithoutKey = builder.toUriString();
            String url = urlWithoutKey + "&serviceKey=" + apiConfig.getServiceKey();

            URI uri = new URI(url);

            System.out.println("API 호출 URL: " + uri.toString());

            String jsonResponse = restTemplate.getForObject(uri, String.class);

            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode dataNode = root.path("data");

            if (dataNode.isArray()) {
                List<JsonNode> list = new ArrayList<>();
                for (JsonNode node : dataNode) {
                    list.add(node);
                }
                return list;
            }

        } catch (IOException | java.net.URISyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public List<ScoreWinnerDTO> getScoreWinnersByRegion(String regionCode) {
        String statApiPath = "/api/ApplyhomeStatSvc/v1/getAPTApsPrzwnerStat";

        List<JsonNode> jsonNodes = fetchDataFromApi(
                statApiPath,
                1,
                10,
                "cond[SUBSCRPT_AREA_CODE::EQ]",
                regionCode
        );

        List<ScoreWinnerDTO> result = new ArrayList<>();
        for (JsonNode node : jsonNodes) {
            try {
                result.add(objectMapper.treeToValue(node, ScoreWinnerDTO.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<ApartmentScoreDTO> getTop5ApartmentsWithLowestScore(String regionCode) {
        String scoreApiPath = "/api/ApplyhomeInfoCmpetRtSvc/v1/getAptLttotPblancScore";

        List<JsonNode> jsonNodes = fetchDataFromApi(
                scoreApiPath,
                1,
                5,
                "cond[RESIDE_SECD::EQ]",
                regionCode
        );

        List<ApartmentScoreDTO> result = new ArrayList<>();
        for (JsonNode node : jsonNodes) {
            try {
                ApartmentScoreDTO dto = objectMapper.treeToValue(node, ApartmentScoreDTO.class);

                String houseName = fetchApartmentName(dto.getHouseManageNo(), dto.getPublicNoticeNo());
                dto.setHouseName(houseName);

                result.add(dto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    // 주택 이름 별도로 받기
    private String fetchApartmentName(String houseManageNo, String pblancNo) {
        String detailApiPath = "/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail";

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl() + detailApiPath)
                    .queryParam("HOUSE_MANAGE_NO", houseManageNo)
                    .queryParam("PBLANC_NO", pblancNo)
                    .queryParam("returnType", "JSON");

            String urlWithoutKey = builder.toUriString();
            String url = urlWithoutKey + "&serviceKey=" + apiConfig.getServiceKey();

            URI uri = new URI(url);
            String jsonResponse = restTemplate.getForObject(uri, String.class);

            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode data = root.path("data");
            if (data.isArray() && data.size() > 0) {
                return data.get(0).path("HOUSE_NM").asText(); // 아파트 이름
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
