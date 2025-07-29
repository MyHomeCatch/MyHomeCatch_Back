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
import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiConfig.getBaseUrl() + apiPath)
                    .queryParam("page", page)
                    .queryParam("perPage", perPage)
                    .queryParam("returnType", "JSON");

            if (conditionKey != null && conditionValue != null) {
                builder.queryParam(conditionKey, conditionValue);
            }

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

        // 100개 정도 뽑아서 필터링: API 페이징 고려
        List<JsonNode> jsonNodes = fetchDataFromApi(
                scoreApiPath,
                1,
                100,
                "cond[RESIDE_SECD::EQ]",
                regionCode
        );

        // houseManageNo 별 이름 캐시 맵
        Map<String, String> houseNameCache = new HashMap<>();

        List<ApartmentScoreDTO> allDtos = new ArrayList<>();
        for (JsonNode node : jsonNodes) {
            try {
                ApartmentScoreDTO dto = objectMapper.treeToValue(node, ApartmentScoreDTO.class);

                // 점수 파싱 및 0점 제외
                String rawScore = dto.getLowestScore();
                if (rawScore == null || rawScore.equals("-")) {
                    continue; // 점수 없으면 건너뜀
                }
                double score;
                try {
                    score = Double.parseDouble(rawScore);
                    if (score == 0.0) {
                        continue; // 0점도 제외
                    }
                } catch (NumberFormatException e) {
                    continue; // 파싱 실패 시 건너뜀
                }
                dto.setParsedLowestScore(score);

                // 아파트 이름 캐시에서 꺼내기 or API 호출
                String houseManageNo = dto.getHouseManageNo();
                String houseName = houseNameCache.get(houseManageNo);
                if (houseName == null) {
                    houseName = fetchApartmentName(houseManageNo, dto.getPublicNoticeNo());
                    if (houseName == null || houseName.isEmpty()) {
                        houseName = "아파트코드_" + houseManageNo;
                    }
                    houseNameCache.put(houseManageNo, houseName);
                }

                // 이름 + 코드 조합
                String combinedName = houseName + " (" + houseManageNo + ")";
                dto.setHouseName(combinedName);

                log.info("Apartment name set (cached): " + combinedName);

                allDtos.add(dto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 아파트 이름 기준으로 그룹화 후, 최저 점수 선택
        Map<String, ApartmentScoreDTO> lowestByApt = allDtos.stream()
                .collect(Collectors.toMap(
                        ApartmentScoreDTO::getHouseName,
                        dto -> dto,
                        (a, b) -> a.getParsedLowestScore() <= b.getParsedLowestScore() ? a : b
                ));

        return lowestByApt.values().stream()
                .sorted(Comparator.comparingDouble(ApartmentScoreDTO::getParsedLowestScore))
                .limit(5)
                .collect(Collectors.toList());
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
