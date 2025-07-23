package org.scoula.applyHome.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.applyHome.domain.ApplyHomeAnalysisVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyHomeAnalysisDTO {
    private Integer cmpetId;
    private String pblancNo;
    private String cmpetRate;
    private String houseManageNo;
    private String houseTy;
    private String modelNo;
    private Integer reqCnt;
    private String resideSecd;
    private String resideSenm;
    private Integer subscrptRankCode;
    private Integer suplyHshldco;
    private String lwetScore;
    private String topScore;
    private String avrgScore;

    // DTO → VO
    public static ApplyHomeAnalysisVO toVO(ApplyHomeAnalysisDTO dto) {
        return ApplyHomeAnalysisVO.builder()
                .cmpetId(dto.getCmpetId())
                .pblancNo(dto.getPblancNo())
                .cmpetRate(dto.getCmpetRate())
                .houseManageNo(dto.getHouseManageNo())
                .houseTy(dto.getHouseTy())
                .modelNo(dto.getModelNo())
                .reqCnt(dto.getReqCnt())
                .resideSecd(dto.getResideSecd())
                .resideSenm(dto.getResideSenm())
                .subscrptRankCode(dto.getSubscrptRankCode())
                .suplyHshldco(dto.getSuplyHshldco())
                .lwetScore(dto.getLwetScore())
                .topScore(dto.getTopScore())
                .avrgScore(dto.getAvrgScore())
                .build();
    }

    // VO → DTO
    public static ApplyHomeAnalysisDTO toDTO(ApplyHomeAnalysisVO vo) {
        return ApplyHomeAnalysisDTO.builder()
                .cmpetId(vo.getCmpetId())
                .pblancNo(vo.getPblancNo())
                .cmpetRate(vo.getCmpetRate())
                .houseManageNo(vo.getHouseManageNo())
                .houseTy(vo.getHouseTy())
                .modelNo(vo.getModelNo())
                .reqCnt(vo.getReqCnt())
                .resideSecd(vo.getResideSecd())
                .resideSenm(vo.getResideSenm())
                .subscrptRankCode(vo.getSubscrptRankCode())
                .suplyHshldco(vo.getSuplyHshldco())
                .lwetScore(vo.getLwetScore())
                .topScore(vo.getTopScore())
                .avrgScore(vo.getAvrgScore())
                .build();
    }
}
