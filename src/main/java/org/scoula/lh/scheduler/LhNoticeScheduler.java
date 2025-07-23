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
import org.scoula.lh.dto.lhHousing.LhHousingApplyDTO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.scoula.lh.dto.lhHousing.LhHousingDTO;
import org.scoula.lh.dto.lhRental.LhRentalApplyDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;
import org.scoula.lh.dto.lhRental.LhRentalDTO;
import org.scoula.lh.dto.rentalNoticeDetailApi.RentalDsSbdAhflDTO;
import org.scoula.lh.dto.rentalNoticeDetailApi.RentalDsSbdDTO;
import org.scoula.lh.dto.rentalNoticeDetailApi.RentalDsSplScdlDTO;
import org.scoula.lh.dto.rentalNoticeDetailApi.RentalNoticeDetailApiDTO;
import org.scoula.lh.service.*;
import org.scoula.lh.service.lhHousing.LhHousingApplyService;
import org.scoula.lh.service.lhHousing.LhHousingAttService;
import org.scoula.lh.service.lhHousing.LhHousingService;
import org.scoula.lh.service.lhRental.LhRentalApplyService;
import org.scoula.lh.service.lhRental.LhRentalAttService;
import org.scoula.lh.service.lhRental.LhRentalService;
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
    private final HashSet<String> housingNoticeCode = new HashSet<>(Arrays.asList("050", "060", "390"));
    private final HashSet<String> RentalNoticeCode = new HashSet<>(Arrays.asList("051", "061", "062", "063"));

    private final LhNoticeSchedulerService lhNoticeSchedulerService;
    private final NoticeAttService noticeAttService;
    private final LhHousingService lhHousingService;
    private final LhHousingApplyService lhHousingApplyService;
    private final LhHousingAttService lhHousingAttService;
    private final LhRentalService lhRentalService;
    private final LhRentalApplyService lhRentalApplyService;
    private final LhRentalAttService lhRentalAttService;

    //    @Scheduled(cron = "0/20 * * * * ?")  // 초 분 시 일 월 요일 (6개 필드!)
//    @Scheduled(fixedDelay = 6000000, initialDelay = 0)
    @Scheduled(cron = "0 0 2 * * *")
    public void schedule() {
        log.info("=== LH 공고 데이터 업데이트 스케줄러 시작 ===");

        // 새로 추가된 notice들만 반환받도록 수정
        List<NoticeDTO> newNotices = updateLhNotices();

        // 새로 추가된 notice들에 대해서만 상세 정보 API 호출
        for(NoticeDTO notice : newNotices) {
            processNoticeDetails(notice);
        }

        log.info("=== LH 공고 데이터 업데이트 스케줄러 완료 ===");
    }

    public List<NoticeDTO> updateLhNotices() {
        try {
            List<NoticeApiDTO> allNotices = fetchLhNotice(10);
            if (allNotices == null || allNotices.isEmpty()) {
                log.warn("가져온 공고 데이터가 없습니다.");
                return new ArrayList<>();
            }

            // 새로 추가된 notice들만 반환
            List<NoticeDTO> newNotices = lhNoticeSchedulerService.createAllAndReturnNew(allNotices);
            log.info("새로 추가된 공고 수: {}", newNotices.size());
            return newNotices;

        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private void processNoticeDetails(NoticeDTO notice) {
        // 주택 공고 처리
        if ((notice.getSplInfTpCd().equals("064") && notice.getCcrCnntSysDsCd().equals("02"))
                || housingNoticeCode.contains(notice.getSplInfTpCd())) {
            processHousingNoticeDetails(notice);
        }

        // 임대 공고 처리
        if ((notice.getSplInfTpCd().equals("064") && notice.getCcrCnntSysDsCd().equals("03"))
                || RentalNoticeCode.contains(notice.getSplInfTpCd())) {
            processRentalNoticeDetails(notice);
        }
    }

    private void processHousingNoticeDetails(NoticeDTO notice) {
        try {
            log.info("주택 공고 상세 정보 처리 시작: {}", notice.getPanId());
            HousingNoticeDetailApiDTO noticeDetail = fetchLhHousingNoticeDetail(notice.getPanId());

            if (noticeDetail == null) {
                log.warn("주택 공고 상세 정보를 가져올 수 없음: {}", notice.getPanId());
                return;
            }

            // 첨부파일 정보 처리
            if (noticeDetail.getDsAhflInfo() != null) {
                for (DsAhflInfoDTO dsAhflInfoDTO : noticeDetail.getDsAhflInfo()) {
                    noticeAttService.create(dsAhflInfoDTO.toVo(notice.getPanId()));
                }
            }

            // 주택 정보 처리
            if (noticeDetail.getDsSbd() != null) {
                for (DsSbdDTO dsSbdDTO : noticeDetail.getDsSbd()) {
                    lhHousingService.create(dsSbdDTO.toLhHousingDTO(notice.getPanId()));
                }
            }

            // 신청 일정 정보 처리
            if (noticeDetail.getDsSplScdl() != null) {
                for (DsSplScdlDTO dsSplScdlDTO : noticeDetail.getDsSplScdl()) {
                    lhHousingApplyService.create(dsSplScdlDTO.toVO(notice.getPanId()));
                }
            }

            // 주택 첨부 정보 처리
            if (noticeDetail.getDsSbdAhfl() != null) {
                for (DsSbdAhflDTO dsSbdAhflDTO : noticeDetail.getDsSbdAhfl()) {
                    lhHousingAttService.create(dsSbdAhflDTO.toLhHousingAttVO(notice.getPanId()));
                }
            }

            log.info("주택 공고 상세 정보 처리 완료: {}", notice.getPanId());

        } catch (Exception e) {
            log.error("주택 공고 상세 정보 처리 중 오류 발생 - PanId: {}, Error: {}",
                    notice.getPanId(), e.getMessage(), e);
        }
    }

    private void processRentalNoticeDetails(NoticeDTO notice) {
        try {
            log.info("임대 공고 상세 정보 처리 시작: {}", notice.getPanId());
            RentalNoticeDetailApiDTO noticeDetail = fetchLhRentalNoticeDetail(notice.getPanId());

            if (noticeDetail == null) {
                log.warn("임대 공고 상세 정보를 가져올 수 없음: {}", notice.getPanId());
                return;
            }

            // 임대 정보 처리
            if (noticeDetail.getDsSbd() != null) {
                for (RentalDsSbdDTO rentalDsSbdDTO : noticeDetail.getDsSbd()) {
                    lhRentalService.create(rentalDsSbdDTO.toLhRentalVO(notice.getPanId()));
                }
            }

            // 임대 신청 정보 처리
            if (noticeDetail.getDsSplScdl() != null) {
                for (RentalDsSplScdlDTO dsSplScdlDTO : noticeDetail.getDsSplScdl()) {
                    lhRentalApplyService.create(dsSplScdlDTO.toLhRentalApplyVO(notice.getPanId()));
                }
            }

            // 임대 첨부 정보 처리
            if (noticeDetail.getDsSbdAhfl() != null) {
                for (RentalDsSbdAhflDTO rentalDsSbdAhflDTO : noticeDetail.getDsSbdAhfl()) {
                    lhRentalAttService.create(rentalDsSbdAhflDTO.toLhRentalAttVO(notice.getPanId()));
                }
            }

            log.info("임대 공고 상세 정보 처리 완료: {}", notice.getPanId());

        } catch (Exception e) {
            log.error("임대 공고 상세 정보 처리 중 오류 발생 - PanId: {}, Error: {}",
                    notice.getPanId(), e.getMessage(), e);
        }
    }

    public List<NoticeApiDTO> fetchLhNotice(int pages) {
        List<String> uppAisTpCodes = Arrays.asList("05", "06");

        RestTemplate restTemplate = new RestTemplate();
        List<NoticeApiDTO> dsList = new ArrayList<>();

        for(int page = 1; page <= pages; page++) {
            for(String uppAisTpCode : uppAisTpCodes) {
                String url = UriComponentsBuilder
                        .fromHttpUrl("http://apis.data.go.kr/B552555/lhLeaseNoticeInfo1/lhLeaseNoticeInfo1")
                        .queryParam("serviceKey", LH_API_SERVICE_KEY)
                        .queryParam("PG_SZ", 14)
                        .queryParam("PAGE", page)
                        .queryParam("UPP_AIS_TP_CD", uppAisTpCode)
                        .build(false)
                        .toUriString();
                try {
                    String res = restTemplate.getForObject(new URI(url), String.class);

                    if (res != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode rootArray = mapper.readTree(res);

                        for (JsonNode node : rootArray) {
                            if (node.has("dsList")) {
                                List<NoticeApiDTO> temp = mapper.convertValue(node.get("dsList"),
                                        new TypeReference<List<NoticeApiDTO>>() {});

                                dsList.addAll(temp);
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
                }
            }
        }

        log.info("파싱 완료: {}개 공고", dsList.size());
        return dsList;
    }

    public HousingNoticeDetailApiDTO fetchLhHousingNoticeDetail(String panId) {
        NoticeDTO NoticeDto = lhNoticeSchedulerService.getNotice(panId);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        QueryParamDTO queryParamDTO = NoticeDto.toQueryParamDTO();
        String url = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B552555/lhLeaseNoticeDtlInfo1/getLeaseNoticeDtlInfo1")
                .queryParam("serviceKey", LH_API_SERVICE_KEY)
                .queryParam("PAN_ID", queryParamDTO.getPanId())
                .queryParam("SPL_INF_TP_CD", queryParamDTO.getSplInfTpCd())
                .queryParam("CCR_CNNT_SYS_DS_CD", queryParamDTO.getCcrCnntSysDsCd())
                .queryParam("AIS_TP_CD", queryParamDTO.getAisTpCd())
                .queryParam("UPP_AIS_TP_CD", queryParamDTO.getUppAisTpCd())
                .build(false)
                .toUriString();

        try {
            log.info("Housing Notice Detail API 호출 시작... panId: {}", queryParamDTO.getPanId());
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

    public RentalNoticeDetailApiDTO fetchLhRentalNoticeDetail(String panId) {
        NoticeDTO NoticeDto = lhNoticeSchedulerService.getNotice(panId);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        QueryParamDTO queryParamDTO = NoticeDto.toQueryParamDTO();
        String url = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B552555/lhLeaseNoticeDtlInfo1/getLeaseNoticeDtlInfo1")
                .queryParam("serviceKey", LH_API_SERVICE_KEY)
                .queryParam("PAN_ID", queryParamDTO.getPanId())
                .queryParam("SPL_INF_TP_CD", queryParamDTO.getSplInfTpCd())
                .queryParam("CCR_CNNT_SYS_DS_CD", queryParamDTO.getCcrCnntSysDsCd())
                .queryParam("AIS_TP_CD", queryParamDTO.getAisTpCd())
                .queryParam("UPP_AIS_TP_CD", queryParamDTO.getUppAisTpCd())
                .build(false)
                .toUriString();

        try {
            log.info("Rental Notice Detail API 호출 시작... panId: {}", queryParamDTO.getPanId());
            String res = restTemplate.getForObject(new URI(url), String.class);

            if (res != null) {
                // JsonNode로 직접 두 번째 요소에 접근
                JsonNode rootNode = objectMapper.readTree(res);

                if (rootNode.isArray() && rootNode.size() >= 2) {
                    JsonNode secondElement = rootNode.get(1); // 두 번째 요소
                    RentalNoticeDetailApiDTO result = objectMapper.treeToValue(secondElement, RentalNoticeDetailApiDTO.class);
                    return result;
                } else {
                    log.warn("응답 배열에 두 번째 요소가 없음 - panId: {}, 배열 크기: {}",
                            panId, rootNode.size());
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생 - panId: {}, 오류: {}", panId, e.getMessage(), e);
        }

        return null;
    }
}