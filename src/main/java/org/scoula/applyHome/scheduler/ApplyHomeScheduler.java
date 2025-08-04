package org.scoula.applyHome.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.applyHome.domain.ApplyHomeAnalysisVO;
import org.scoula.applyHome.domain.ApplyHomeSpecialVO;
import org.scoula.applyHome.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.scoula.applyHome.service.ApplyHomeService;
import org.scoula.applyHome.service.ApplyHomeServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Arrays.stream;

@Log4j2
@RequiredArgsConstructor
@Component
@PropertySource("classpath:application.properties")
public class ApplyHomeScheduler {
    @Value("${APPLYHOME_API_SERVICE_KEY}")
    private String APPLYHOME_API_SERVICE_KEY;

    private final ApplyHomeService service;

    // 테스트에서 키를 주입할 수 있도록 setter 추가
    public void setApplyhomeApiServiceKey(String key) {
        this.APPLYHOME_API_SERVICE_KEY = key;
    }

    // 새벽 2시마다 실행 (초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 2 * * *")
    public void schedule() throws Exception {
        log.info("Scheduling apply home analysis");

        List<ApplyHomeDTO> newNotices = fetchApplyHomeNotice(1);
        newNotices.forEach(service::create);

        List<ApplyHomeAnalysisDTO> competList = fetchApplyHomeCompet(1);
        List<ApplyHomeAnalysisDTO> winnerList = fetchApplyHomeWinner(1);
        List<ApplyHomeSpecialDTO> specialList = fetchApplyHomeSpecial(1);
        specialList.forEach(service::createSpecial);
    }

    public void fetchNotices() {
        log.info("청약홈 공고 데이터 적재");

        try {
            List<ApplyHomeDTO> allNotices = fetchApplyHomeNotice(250); //2455 - 100
            allNotices.forEach(service::create);

            // 경쟁률 및 당첨가점 정보
            // return하는게 안들어와서 내부에서 바로 Service::create)
            List<ApplyHomeAnalysisDTO> competList = fetchApplyHomeCompet(465); //46488 - 100
            List<ApplyHomeAnalysisDTO> winnerList = fetchApplyHomeWinner(253); // 25237 - 100

            List<ApplyHomeSpecialDTO> specialList = fetchApplyHomeSpecial(1);

        } catch (Exception e) {
            log.error("청약홈 스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
        }
        log.info("청약홈 공고 데이터 업데이트 스케줄러 완료");
    }


    public List<ApplyHomeDTO> fetchApplyHomeNotice(int pages) throws Exception {
        log.info("청약홈 공고 데이터 업데이트");
        RestTemplate restTemplate = new RestTemplate();
        List<ApplyHomeDTO> allNotices = new ArrayList<>();

        String raw = "cond[HOUSE_SECD::EQ]";
        String encodedCondition = UriUtils.encodeQueryParam(raw, StandardCharsets.UTF_8);

        for (int page = 1; page <= pages; page++) {
            // 분양정보 상세 조회
            String url = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail")
                    .queryParam("page", page)
                    .queryParam("perPage", 100)
                    .queryParam(encodedCondition, "01")
                    .queryParam("serviceKey", APPLYHOME_API_SERVICE_KEY)
                    .build(false)
                    .toUriString();

            log.debug("url: {}", url);

            try {
                String res = restTemplate.getForObject(new URI(url), String.class);

                if (res != null) {
                    ObjectMapper mapper = new ObjectMapper()
                            // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
                            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                            // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    JsonNode root = mapper.readTree(res);

                    // "data" 필드를 꺼내서 List<ApplyHomeDTO>로 변환
                    JsonNode dataNode = root.path("data");
                    if (dataNode.isMissingNode()    // no “data” field
                            || dataNode.isNull()          // “data”: null
                            || (dataNode.isArray() && dataNode.size() == 0)  // “data”: []
                    ) break;
                    else {
                        List<ApplyHomeDTO> dto = mapper.convertValue(dataNode, new TypeReference<List<ApplyHomeDTO>>() {
                        });
                        allNotices.addAll(dto);
                    }

                }
            } catch (Exception e) {
                log.error("청약홈 공고 API 호출 중 오류 발생: {}" + e.getMessage(), e);
            }
        }
        log.info("청약홈 공고 API 파싱 완료: {}개 공고", allNotices.size());
        log.info("allNotices: {}", allNotices);
        return allNotices;
    }


    public List<ApplyHomeAnalysisDTO> fetchApplyHomeCompet(int pages) throws Exception {
        log.info("청약홈 공고 경쟁률 업데이트");  // 46488
        RestTemplate restTemplate = new RestTemplate();
        List<ApplyHomeAnalysisDTO> allNotices = new ArrayList<>();

        for (int page = 1; page <= pages; page++) {
            // 경쟁률 정보
            String url_cmpet = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/ApplyhomeInfoCmpetRtSvc/v1/getAPTLttotPblancCmpet")
                    .queryParam("page", page)
                    .queryParam("perPage", 100)
                    .queryParam("serviceKey", APPLYHOME_API_SERVICE_KEY)
                    .build(false)
                    .toUriString();

            try {
                String res = restTemplate.getForObject(new URI(url_cmpet), String.class);
                ObjectMapper mapper = new ObjectMapper()
                        // 필드 이름 변환 전략 추가
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
                        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                        // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode root = mapper.readTree(res);
                JsonNode dataNode = root.path("data");
                if (dataNode.isMissingNode() || dataNode.isNull() || (dataNode.isArray() && dataNode.size() == 0))
                    break;

                List<ApplyHomeAnalysisDTO> dto = mapper.convertValue(dataNode, new TypeReference<List<ApplyHomeAnalysisDTO>>() {
                });
                allNotices.addAll(dto);
            } catch (Exception e) {
                log.error("청약홈 경쟁률 API 호출 중 오류 발생: {}" + e.getMessage(), e);
            }
        }
        log.info("Successfully parsed {} items from API response.", allNotices.size());
        allNotices.forEach(service::createAnalysis);
        return allNotices;
    }


    public List<ApplyHomeAnalysisDTO> fetchApplyHomeWinner(int pages) throws Exception {
        log.info("청약홈 당첨가점 업데이트"); // 25500
        RestTemplate restTemplate = new RestTemplate();
        List<ApplyHomeAnalysisDTO> allNotices = new ArrayList<>();

        for (int page = 1; page <= pages; page++) {

            // 당첨자 가점
            String url_winner_score = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/ApplyhomeInfoCmpetRtSvc/v1/getAptLttotPblancScore")
                    .queryParam("page", page)
                    .queryParam("perPage", 100)
                    .queryParam("serviceKey", APPLYHOME_API_SERVICE_KEY)
                    .build(false)
                    .toUriString();

            try {
                String res = restTemplate.getForObject(new URI(url_winner_score), String.class);
                ObjectMapper mapper = new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
                        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                        // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode root = mapper.readTree(res);
                JsonNode dataNode = root.path("data");

                if (dataNode.isMissingNode() || dataNode.isNull() || (dataNode.isArray() && dataNode.size() == 0))
                    break;

                List<ApplyHomeAnalysisDTO> dto = mapper.convertValue(dataNode, new TypeReference<List<ApplyHomeAnalysisDTO>>() {
                });
                allNotices.addAll(dto);

            } catch (Exception e) {
                log.error("청약홈 당첨가점 API 호출 중 오류 발생: {}" + e.getMessage(), e);
            }
        }
        log.info("Successfully parsed {} items from 당첨가점 API response.", allNotices.size());
        allNotices.forEach(service::createAnalysis);
        return allNotices;
    }

    public List<ApplyHomeSpecialDTO> fetchApplyHomeSpecial(int pages) throws Exception {
        log.info("청약홈 분양정보 주택형별 업데이트");  // 46488
        RestTemplate restTemplate = new RestTemplate();
        List<ApplyHomeSpecialDTO> allNotices = new ArrayList<>();

        for (int page = 1; page <= pages; page++) {
            // 경쟁률 정보
            String url_cmpet = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancMdl")
                    .queryParam("page", page)
                    .queryParam("perPage", 100)
                    .queryParam("serviceKey", APPLYHOME_API_SERVICE_KEY)
                    .build(false)
                    .toUriString();

            try {
                String res = restTemplate.getForObject(new URI(url_cmpet), String.class);
                ObjectMapper mapper = new ObjectMapper()
                        // 필드 이름 변환 전략 추가
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
                        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                        // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode root = mapper.readTree(res);
                JsonNode dataNode = root.path("data");
                if (dataNode.isMissingNode() || dataNode.isNull() || (dataNode.isArray() && dataNode.size() == 0))
                    break;

                List<ApplyHomeSpecialDTO> dto = mapper.convertValue(dataNode, new TypeReference<List<ApplyHomeSpecialDTO>>() {
                });
                allNotices.addAll(dto);
            } catch (Exception e) {
                log.error("청약홈 경쟁률 API 호출 중 오류 발생: {}" + e.getMessage(), e);
            }
        }
        log.info("Successfully parsed {} items from 주택형별 상세조회 API response.", allNotices.size());
        return allNotices;
    }
}


