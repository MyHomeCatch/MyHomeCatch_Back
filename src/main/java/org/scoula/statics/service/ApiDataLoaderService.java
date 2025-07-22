package org.scoula.statics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.WinnerRegionVO;
import org.scoula.statics.mapper.StaticsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApiDataLoaderService {

    private final StaticsMapper mapper;
    @Value("${api-key}")
    private String serviceKey;

    // 공공데이터 api -> DB 저장
    public void applicantApiSave(String regionCode, int year) {
        System.out.println("call api");
        String apiUrl = "https://api.odcloud.kr/api/ApplyhomeStatSvc/v1/getAPTReqstAreaStat";
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?page=1&perPage=100");
        urlBuilder.append("&cond[STAT_DE::LTE]=").append(Integer.toString(year+1));
        urlBuilder.append("&cond[STAT_DE::GTE]=").append(Integer.toString(year));
        urlBuilder.append("&serviceKey=").append(URLEncoder.encode(serviceKey, StandardCharsets.UTF_8));

        System.out.println("------------");
        System.out.println(urlBuilder.toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);
        System.out.println("------------");
        System.out.println(response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper object = new ObjectMapper();
                JsonNode dataNode = object.readTree(response.getBody()).get("data");

                if (dataNode != null && dataNode.isArray()) {
                    for (JsonNode node : dataNode) {
                        ApplicantRegionVO vo = applicantOf(node);
                        mapper.insertApplicantRegion(vo);
                    }
                }
            } catch (Exception e) {
                log.error("API 응답 파싱 오류: " + e.getMessage());
            }
        }
    }

    public void winnerApiSave(String regionCode, int year) {
        String apiUrl = "https://api.odcloud.kr/api/ApplyhomeStatSvc/v1/getAPTPrzwnerAreaStat";
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?page=1&perPage=100");
        urlBuilder.append("&cond[STAT_DE::GTE]=").append(year);
        urlBuilder.append("&serviceKey=").append(serviceKey);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper object = new ObjectMapper();
                JsonNode dataNode = object.readTree(response.getBody()).get("data");

                if (dataNode != null && dataNode.isArray()) {
                    for (JsonNode node : dataNode) {
                        ApplicantRegionVO vo = applicantOf(node);
                        mapper.insertApplicantRegion(vo);
                    }
                }
            } catch (Exception e) {
                log.error("API 응답 파싱 오류: " + e.getMessage());
            }
        }
    }

    private ApplicantRegionVO applicantOf(JsonNode node) {
        long regionId = mapper.getRegionId(node.get("SUBSCRPT_AREA_CODE").asText());
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
        long regionId = mapper.getRegionId(node.get("SUBSCRPT_AREA_CODE").asText());
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
