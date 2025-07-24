package org.scoula.statics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final ApiConfig apiConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
                result.add(objectMapper.treeToValue(node, ApartmentScoreDTO.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
