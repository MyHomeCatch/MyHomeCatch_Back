package org.scoula.lh.danzi.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.lh.danzi.dto.EligibilityResultDTO;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;
import org.scoula.lh.danzi.dto.http.PersonalizedCardDTO;
import org.scoula.lh.danzi.mapper.PersonalSummaryMapper;
import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.scoula.selfCheck.mapper.SelfCheckMapper;
import org.scoula.summary.mapper.SummaryMapper;
import org.scoula.summary.service.ParsedSummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PersonalizedCardServiceImpl implements PersonalizedService {

    private final ParsedSummaryService parsedSummaryService;
    private final SummaryMapper summaryMapper;
    private final EligibilityService eligibilityService;
    private final SelfCheckMapper userSelfCheckMapper;
    private final PersonalSummaryMapper personalSummaryMapper;

    /**
     * danziId, userId, markdown을 받아 저장 + 분석 + DTO 반환
     */
    @Transactional
    @Override
    public PersonalizedCardDTO getOrCreatePersonalCard(int danziId, int userId) {
        String markdown = summaryMapper.findByPanId(danziId);

        // 1) 마크다운 → 요약 DTO 저장 및 가져오기
        NoticeSummaryDTO summary = parsedSummaryService.createFromMarkdown(danziId, markdown);

        if (summary == null) {
            return null;
        }

        log.info("summary: {}", summary);

        // 2) 사용자 자기진단 로드
        SelfCheckContentDto user = userSelfCheckMapper.findSelfCheckContentByUserId(userId);

        if (user == null) {
            EligibilityResultDTO result = new EligibilityResultDTO();
            result.setOverallStatus(EligibilityResultDTO.EligibilityStatus.NEEDS_REVIEW);
            result.addNote("자격진단이 필요합니다.");

            return PersonalizedCardDTO.builder()
                    .userId(userId)
                    .danziId(danziId)
                    .eligibilityResultDTO(result)
                    .build();
        }

        // 3) 적합성 분석
        EligibilityResultDTO result = eligibilityService.analyze(summary, user);

        // 4) 퍼스널라이즈 카드
        PersonalizedCardDTO out = new PersonalizedCardDTO();
        out.setDanziId(danziId);
        out.setUserId(userId);
        out.setEligibilityResultDTO(result);

        personalSummaryMapper.upsert(out);
        return out;
    }

    @Transactional
    @Override
    public PersonalizedCardDTO getOrCreatePersonalCardFromJson(int danziId, int userId) {
        String json = summaryMapper.findJsonByDanziId(danziId);

        // 1) 요약 DTO 저장 및 가져오기
        JsonSummaryDTO summary = parsedSummaryService.createFromJson(danziId, json);

        if (summary == null) {
            return null;
        }

        log.info("summary: {}", summary);

        // 2) 사용자 자기진단 로드
        SelfCheckContentDto user = userSelfCheckMapper.findSelfCheckContentByUserId(userId);

        if (user == null) {
            EligibilityResultDTO result = new EligibilityResultDTO();
            result.setOverallStatus(EligibilityResultDTO.EligibilityStatus.NEEDS_REVIEW);
            result.addNote("자격진단이 필요합니다.");

            return PersonalizedCardDTO.builder()
                    .userId(userId)
                    .danziId(danziId)
                    .eligibilityResultDTO(result)
                    .build();
        }

        // 3) 적합성 분석
        EligibilityResultDTO result = eligibilityService.analyzeJson(summary, user);
        log.info("result: {}", result);

        // 4) 퍼스널라이즈 카드
        PersonalizedCardDTO out = new PersonalizedCardDTO();
        out.setDanziId(danziId);
        out.setUserId(userId);
        out.setEligibilityResultDTO(result);

        personalSummaryMapper.upsert(out);
        return out;
    }
}
