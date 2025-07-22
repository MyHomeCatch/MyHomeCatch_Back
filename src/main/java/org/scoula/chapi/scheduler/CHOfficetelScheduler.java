package org.scoula.chapi.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.chapi.dto.CHOfficetelCmpetDTO;
import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.dto.CHOfficetelModelDTO;
import org.scoula.chapi.service.OfficetelDBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
@PropertySource("classpath:application.properties")
public class CHOfficetelScheduler {

    @Value("${applyHome.api_key}")
    private String applyHomeApiServiceKey;

    private final OfficetelDBService officetelDBService;

    // 테스트에서 키를 주입할 수 있도록 setter 추가
    public void setApplyHomeApiServiceKey(String key) {
        this.applyHomeApiServiceKey = key;
    }

    // 자정마다 실행 (초 분 시 일 월 요일)
//    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchAndUpdateAllOfficetelData(){
        log.info("청약홈 오피스텔 전체 데이터 업데이트 스케줄러 시작");

        fetchAndUpdateOfficetelNotices();
        fetchAndUpdateOfficetelModels();
        fetchAndUpdateOfficetelCmpet();

        log.info("청약홈 오피스텔 전체 데이터 업데이트 스케줄러 완료");
    }


    
    // 오피스텔 공고 기본정보 업데이트
    public void fetchAndUpdateOfficetelNotices() {
        log.info("청약홈 오피스텔 공고 기본정보 업데이트 스케줄러 시작");

        try {
            List<CHOfficetelDTO> allNotices = fetchNoticeFromApi(
                    "ApplyhomeInfoDetailSvc/v1/getUrbtyOfctlLttotPblancDetail",
                    20,
                    new TypeReference<List<CHOfficetelDTO>>() {});
            allNotices.forEach(officetelDBService::insert);
            log.info("오피스텔 공고 기본정보 업데이트 완료: {}건", allNotices.size());
        } catch (Exception e) {
            log.error("오피스텔 공고 기본정보 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }

        log.info("청약홈 오피스텔 공고 기본정보 업데이트 스케줄러 완료");
    }

    // 오피스텔 공고 주택형별 상세정보 업데이트
    public void fetchAndUpdateOfficetelModels() {
        log.info("청약홈 오피스텔 공고 주택형별 상세정보 업데이트 스케줄러 시작");

        try {
            List<CHOfficetelModelDTO> allNotices = fetchNoticeFromApi(
                    "ApplyhomeInfoDetailSvc/v1/getUrbtyOfctlLttotPblancMdl",
                    20,
                    new TypeReference<List<CHOfficetelModelDTO>>() {});
            allNotices.forEach(officetelDBService::insertOfficetelModel);
            log.info("오피스텔 공고 주택형별 상세정보 업데이트 완료: {}건", allNotices.size());
        } catch (Exception e) {
            log.error("오피스텔 공고 주택형별 상세정보 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }

        log.info("청약홈 오피스텔 공고 주택형별 상세정보 업데이트 스케줄러 완료");
    }

    // 오피스텔 공고 주택형별 상세정보 업데이트
    public void fetchAndUpdateOfficetelCmpet() {
        log.info("청약홈 오피스텔 공고 경쟁률 상세정보 업데이트 스케줄러 시작");

        try {
            List<CHOfficetelCmpetDTO> allNotices = fetchNoticeFromApi(
                    "ApplyhomeInfoCmpetRtSvc/v1/getUrbtyOfctlLttotPblancCmpet",
                    20,
                    new TypeReference<List<CHOfficetelCmpetDTO>>() {});
            allNotices.forEach(officetelDBService::insertOfficetelCmpet);
            log.info("오피스텔 공고 경쟁률 상세정보 업데이트 완료: {}건", allNotices.size());
        } catch (Exception e) {
            log.error("오피스텔 공고 경쟁률 상세정보 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }

        log.info("청약홈 오피스텔 공고 주택형별 경쟁률 업데이트 스케줄러 완료");
    }



    // API 호출을 일반화한 제네릭 메서드
    public <T> List<T> fetchNoticeFromApi(String apiEndpoint, int size, TypeReference<List<T>> typeReference) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/" + apiEndpoint)
                .queryParam("page", 1)
                .queryParam("perPage", size)
                .queryParam("serviceKey", applyHomeApiServiceKey)
                .build(false)
                .toUriString();
        log.debug("url: {}", url);

        try {
            String res = restTemplate.getForObject(new URI(url), String.class);
            ObjectMapper mapper = new ObjectMapper()
                    // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode root = mapper.readTree(res);

            // "data" 필드를 꺼내서 List<개별 DTO>로 변환
            JsonNode dataNode = root.path("data");
            if (dataNode.isArray()) {
                return mapper.convertValue(
                        dataNode, typeReference);
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: (Endpoint: {}): {}", apiEndpoint, e.getMessage(), e);
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

//    public List<CHOfficetelDTO> fetchNotice(int size) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        String url = UriComponentsBuilder.fromHttpUrl("https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail")
//                .queryParam("page", 1)
//                .queryParam("perPage", size)
//                .queryParam("serviceKey", applyHomeApiServiceKey)
//                .build(false)
//                .toUriString();
//        log.debug("url: {}", url);
//
//        List<CHOfficetelDTO> applyHomeOfficetel = null;
//
//        try {
//            String res = restTemplate.getForObject(new URI(url), String.class);
//            ObjectMapper mapper = new ObjectMapper()
//                    // JSON 필드를 자바 프로퍼티에 대소문자 구분 없이 매핑
//                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
//                    // DTO에 없는 필드는 무시 (UnrecognizedPropertyException 방지)
//                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            JsonNode root = mapper.readTree(res);
//
//            // "data" 필드를 꺼내서 List<CHOfficetelDTO>로 변환
//            JsonNode dataNode = root.path("data");
//            if (dataNode.isArray()) {
//                return mapper.convertValue(
//                        dataNode,
//                        new TypeReference<List<CHOfficetelDTO>>() {});
//
//            }
//        } catch (Exception e) {
//            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return Collections.emptyList();
//    }
}
