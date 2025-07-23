package org.scoula.lh.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.dto.*;
import org.scoula.lh.dto.housingNoticeDetailApi.*;
import org.scoula.lh.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@PropertySource("classpath:application.properties")
@Log4j2
@Component
@RequiredArgsConstructor
public class LhNoticeScheduler {
    @Value("${LH_API_SERVICE_KEY}")
    private String LH_API_SERVICE_KEY;
    private final HashSet<String> housingNoticeCode = new HashSet<>(Arrays.asList("050"));

    private final LhNoticeSchedulerService lhNoticeSchedulerService;
    private final NoticeAttService noticeAttService;
    private final LhHousingService lhHousingService;
    private final LhHousingApplyService lhHousingApplyService;
    private final LhHousingAttService lhHousingAttService;


    @Scheduled(cron = "0/20 * * * * ?")  // 초 분 시 일 월 요일 (6개 필드!)
    public void schedule() {
        log.info("=== LH 공고 데이터 업데이트 스케줄러 시작 ===");

        updateLhNotices();
        List<NoticeDTO> notices = lhNoticeSchedulerService.getNotices();
        for(NoticeDTO notice : notices) {
            if (housingNoticeCode.contains(notice.getSplInfTpCd())) {
                List<NoticeAttDTO> noticeAttDTOS = noticeAttService.getNoticeAttByPanId(notice.getPanId());
                HousingNoticeDetailApiDTO noticeDetail = fetchLhNoticeDetail(notice.getPanId());

                if(noticeAttDTOS == null || noticeAttDTOS.isEmpty()) {
                    if(noticeDetail.getDsAhflInfo() != null) {
                        for (DsAhflInfoDTO dsAhflInfoDTO : noticeDetail.getDsAhflInfo()) {
                            noticeAttService.create(dsAhflInfoDTO.toVo(notice.getPanId()));
                        }
                    }
                }

                List<LhHousingDTO> housingDTOS = lhHousingService.getByPanId(notice.getPanId());
                if(housingDTOS == null || housingDTOS.isEmpty()) {
                    if(noticeDetail.getDsSbd() != null) {
                        for(DsSbdDTO dsSbdDTO : noticeDetail.getDsSbd()) {
                            lhHousingService.create(dsSbdDTO.toLhHousingDTO(notice.getPanId()));
                        }
                    }
                }

                List<LhHousingApplyDTO> housingApplyDTOS = lhHousingApplyService.getAllByPanId(notice.getPanId());
                if(housingApplyDTOS == null || housingApplyDTOS.isEmpty()) {
                    if(noticeDetail.getDsSplScdl() != null) {
                        for(DsSplScdlDTO dsSplScdlDTO : noticeDetail.getDsSplScdl()) {
                            lhHousingApplyService.create(dsSplScdlDTO.toVO(notice.getPanId()));
                        }
                    }
                }

                List<LhHousingAttDTO> housingAttDTOS = lhHousingAttService.getByPanId(notice.getPanId());
                if(housingAttDTOS == null || housingAttDTOS.isEmpty()) {
                    if(noticeDetail.getDsSbdAhfl() != null) {
                        for(DsSbdAhflDTO dsSbdAhflDTO : noticeDetail.getDsSbdAhfl()) {
                            lhHousingAttService.create(dsSbdAhflDTO.toLhHousingAttVO(notice.getPanId()));
                        }
                    }
                }
            }
        }

        log.info("=== LH 공고 데이터 업데이트 스케줄러 완료 ===");
    }

    public void updateLhNotices() {
        try {
            List<NoticeApiDTO> allNotices = fetchLhNotice(14);
            if (allNotices == null || allNotices.isEmpty()) {
                log.warn("가져온 공고 데이터가 없습니다.");
                return;
            }
            log.info("총 {}개의 공고를 가져왔습니다.", allNotices.size());
            lhNoticeSchedulerService.createAll(allNotices);
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    public List<NoticeApiDTO> fetchLhNotice(int size) {
        int pageSize = Math.max(size, 1);
        int page = 1;
        String UPP_AIS_TP_CD = "05";

        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B552555/lhLeaseNoticeInfo1/lhLeaseNoticeInfo1")
                .queryParam("serviceKey", LH_API_SERVICE_KEY)
                .queryParam("PG_SZ", pageSize)
                .queryParam("PAGE", page)
                .queryParam("UPP_AIS_TP_CD", UPP_AIS_TP_CD)
                .build(false)
                .toUriString();

        List<NoticeApiDTO> dsList = new ArrayList<>();

        try {
            String res = restTemplate.getForObject(new URI(url), String.class);

            if (res != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootArray = mapper.readTree(res);

                for (JsonNode node : rootArray) {
                    if (node.has("dsList")) {
                        dsList = mapper.convertValue(node.get("dsList"),
                                new TypeReference<List<NoticeApiDTO>>() {});
                        log.info("파싱 완료: {}개 공고", dsList.size());
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
        }

        return dsList;
    }

    public HousingNoticeDetailApiDTO fetchLhNoticeDetail(String panId) {
        NoticeDTO NoticeDto = lhNoticeSchedulerService.getNotice(panId);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        QueryParamDTO queryParamDTO = NoticeDto.toQueryParamDTO();
        String url = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B552555/lhLeaseNoticeDtlInfo1/getLeaseNoticeDtlInfo1")
                .queryParam("serviceKey", "EKnz94vtAe4ueCv7XJwzzUrA3HHKz2%2BDTAtvnHJNt5yjoVZMo%2Bqqk1hV%2FApwknZ0slBYc%2BXKooVXHoE8woxEpw%3D%3D")
                .queryParam("PAN_ID", queryParamDTO.getPanId())
                .queryParam("SPL_INF_TP_CD", queryParamDTO.getSplInfTpCd())
                .queryParam("CCR_CNNT_SYS_DS_CD", queryParamDTO.getCcrCnntSysDsCd())
                .queryParam("AIS_TP_CD", queryParamDTO.getAisTpCd())
                .queryParam("UPP_AIS_TP_CD", queryParamDTO.getUppAisTpCd())
                .build(false)
                .toUriString();

        try {
            log.info("Notice Detail API 호출 시작... panId: {}", queryParamDTO.getPanId());
            String res = restTemplate.getForObject(new URI(url), String.class);

            if (res != null) {
                // JsonNode로 직접 두 번째 요소에 접근
                JsonNode rootNode = objectMapper.readTree(res);

                if (rootNode.isArray() && rootNode.size() >= 2) {
                    JsonNode secondElement = rootNode.get(1); // 두 번째 요소
                    HousingNoticeDetailApiDTO result = objectMapper.treeToValue(secondElement, HousingNoticeDetailApiDTO.class);
                    return result;
                } else {
                    log.warn("응답 배열에 두 번째 요소가 없음 - panId: {}, 배열 크기: {}",
                            panId, rootNode.size());
                    return null;
                }
            }

        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패 - panId: {}, 오류: {}", panId, e.getMessage(), e);
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생 - panId: {}, 오류: {}", panId, e.getMessage(), e);
        }

        return null;
    }
}