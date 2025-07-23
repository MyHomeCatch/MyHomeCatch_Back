package org.scoula.applyHome.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.applyHome.domain.ApplyHomeSpecialVO;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplyHomeSpecialDTO {
    @JsonProperty("ETC_HSHLDCO")
    private int etcHshldco;

    @JsonProperty("HOUSE_MANAGE_NO")
    private String houseManageNo;

    @JsonProperty("HOUSE_TY")
    private String houseTy;

    @JsonProperty("INSTT_RECOMEND_HSHLDCO")
    private int insttRecomendHshldco;

    @JsonProperty("LFE_FRST_HSHLDCO")
    private int lfeFrstHshldco;

    @JsonProperty("LTTOT_TOP_AMOUNT")
    private String lttotTopAmount;

    @JsonProperty("MNYCH_HSHLDCO")
    private int mnychHshldco;

    @JsonProperty("MODEL_NO")
    private String modelNo;

    @JsonProperty("NWBB_HSHLDCO")
    private int nwbbHshldco;

    @JsonProperty("NWWDS_HSHLDCO")
    private int nwwdsHshldco;

    @JsonProperty("OLD_PARNTS_SUPORT_HSHLDCO")
    private int oldParntsSuportHshldco;

    @JsonProperty("PBLANC_NO")
    private String pblancNo;

    @JsonProperty("SPSPLY_HSHLDCO")
    private int spsplyHshldco;

    @JsonProperty("SUPLY_AR")
    private String suplyAr;

    @JsonProperty("SUPLY_HSHLDCO")
    private int suplyHshldco;

    @JsonProperty("TRANSR_INSTT_ENFSN_HSHLDCO")
    private int transrInsttEnfsnHshldco;

    @JsonProperty("YGMN_HSHLDCO")
    private int ygmnHshldco;
    
    public static ApplyHomeSpecialVO toVO(ApplyHomeSpecialDTO dto) {
        if (dto == null) return null;
        return ApplyHomeSpecialVO.builder()
                .etcHshldco(dto.getEtcHshldco())
                .houseManageNo(dto.getHouseManageNo())
                .houseTy(dto.getHouseTy())
                .insttRecomendHshldco(dto.getInsttRecomendHshldco())
                .lfeFrstHshldco(dto.getLfeFrstHshldco())
                .lttotTopAmount(dto.getLttotTopAmount())
                .mnychHshldco(dto.getMnychHshldco())
                .modelNo(dto.getModelNo())
                .nwbbHshldco(dto.getNwbbHshldco())
                .nwwdsHshldco(dto.getNwwdsHshldco())
                .oldParntsSuportHshldco(dto.getOldParntsSuportHshldco())
                .pblancNo(dto.getPblancNo())
                .spsplyHshldco(dto.getSpsplyHshldco())
                .suplyAr(dto.getSuplyAr())
                .suplyHshldco(dto.getSuplyHshldco())
                .transrInsttEnfsnHshldco(dto.getTransrInsttEnfsnHshldco())
                .ygmnHshldco(dto.getYgmnHshldco())
                .build();
    }

    public static ApplyHomeSpecialDTO toDTO(ApplyHomeSpecialVO vo) {
        if (vo == null) return null;
        return ApplyHomeSpecialDTO.builder()
                .etcHshldco(vo.getEtcHshldco())
                .houseManageNo(vo.getHouseManageNo())
                .houseTy(vo.getHouseTy())
                .insttRecomendHshldco(vo.getInsttRecomendHshldco())
                .lfeFrstHshldco(vo.getLfeFrstHshldco())
                .lttotTopAmount(vo.getLttotTopAmount())
                .mnychHshldco(vo.getMnychHshldco())
                .modelNo(vo.getModelNo())
                .nwbbHshldco(vo.getNwbbHshldco())
                .nwwdsHshldco(vo.getNwwdsHshldco())
                .oldParntsSuportHshldco(vo.getOldParntsSuportHshldco())
                .pblancNo(vo.getPblancNo())
                .spsplyHshldco(vo.getSpsplyHshldco())
                .suplyAr(vo.getSuplyAr())
                .suplyHshldco(vo.getSuplyHshldco())
                .transrInsttEnfsnHshldco(vo.getTransrInsttEnfsnHshldco())
                .ygmnHshldco(vo.getYgmnHshldco())
                .build();
    }
}
