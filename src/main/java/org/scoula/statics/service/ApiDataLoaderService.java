package org.scoula.statics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiDataLoaderService {

    private final StaticsMapper mapper;
    @Value("${applyhome.api_key}")
    private String serviceKey;

    // 공공데이터 api -> DB 저장
    public void applicantApiSave(String regionCode, int year) throws IOException, URISyntaxException {
        System.out.println("call api");
        String apiUrl = "https://api.odcloud.kr/api/ApplyhomeStatSvc/v1/getAPTReqstAreaStat";
        int page = 1, perPage = 100;

        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("cond[STAT_DE::LTE]", String.valueOf(year+1))
                .queryParam("cond[STAT_DE::GTE]", String.valueOf(year))
                //.queryParam("serviceKey", serviceKey)
                .toUriString();

        url += "&serviceKey=" + serviceKey;
        System.out.println(url);
        URI uri = new URI(url);
        //ResponseEntity<Map> resultMap = restTemplate.exchange(uri, HttpMethod.POST, null, Map.class);
        String response = restTemplate.getForObject(uri, String.class);
        //System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode dataArray = rootNode.path("data");

        List<ApplicantRegionVO> applicantList = new ArrayList<>();

        for (JsonNode node : dataArray) {
            ApplicantRegionVO applicant = applicantOf(node);
            applicantList.add(applicant);
        }

        mapper.insertApplicantList(applicantList);
    }

    public void winnerApiSave(String regionCode, int year) throws URISyntaxException, IOException {
        String apiUrl = "https://api.odcloud.kr/api/ApplyhomeStatSvc/v1/getAPTPrzwnerAreaStat";
        int page = 1, perPage = 100;

        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("cond[STAT_DE::LTE]", String.valueOf(year+1))
                .queryParam("cond[STAT_DE::GTE]", String.valueOf(year))
                //.queryParam("serviceKey", serviceKey)
                .toUriString();

        url += "&serviceKey=" + serviceKey;
        System.out.println(url);
        URI uri = new URI(url);
        //ResponseEntity<Map> resultMap = restTemplate.exchange(uri, HttpMethod.POST, null, Map.class);
        String response = restTemplate.getForObject(uri, String.class);
        //System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode dataArray = rootNode.path("data");

        List<WinnerRegionVO> winnerList = new ArrayList<>();

        for (JsonNode node : dataArray) {
            WinnerRegionVO winner = winnerOf(node);
            winnerList.add(winner);
        }

        mapper.insertWinnerList(winnerList);
    }

    private ApplicantRegionVO applicantOf(JsonNode node) {
        long regionId = mapper.getRegionId(node.get("SUBSCRPT_AREA_CODE_NM").asText());
        return ApplicantRegionVO.builder()
                .regionId(regionId)
                .statDe(node.get("STAT_DE").asText())
                .age30(node.get("AGE_30").asInt())
                .age40(node.get("AGE_40").asInt())
                .age50(node.get("AGE_50").asInt())
                .age60(node.get("AGE_60").asInt())
                .build();
    }

    private WinnerRegionVO winnerOf(JsonNode node) {
        long regionId = mapper.getRegionId(node.get("SUBSCRPT_AREA_CODE_NM").asText());
        return WinnerRegionVO.builder()
                .regionId(regionId)
                .statDe(node.get("STAT_DE").asText())
                .age30Win(node.get("AGE_30").asInt())
                .age40Win(node.get("AGE_40").asInt())
                .age50Win(node.get("AGE_50").asInt())
                .age60Win(node.get("AGE_60").asInt())
                .build();
    }
}
