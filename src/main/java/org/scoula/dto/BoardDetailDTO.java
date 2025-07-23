package org.scoula.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.Api;
import org.scoula.applyHome.dto.ApplyHomeDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "공고 상세 DTO")
public class BoardDetailDTO {
    @ApiModelProperty(value = "공고 번호")
    private String NOTICE_ID;
    private String title;
    private String status;
    private String bgnde;
    private String endde;
    private String address;
    private String name;
    private String bsnsMby;
    private String cnstrctEntr;
    private String type;
    private Integer downPayment;
    private Integer bookmarkCount;
    private Integer supplyAr;
    private String totalHouseholds;
    private String articleUrl;
    private List<String> facilities;
    private String contact;
    private Map<Integer, Double> compett;
    private List<String> imgUrl;
    private List<CommentDTO> comments;
    private String summ;
    private Date prearngeYm;
    private Date presntnDe;
    private String rdnEarth;
    private Date spsBgnde;
    private Date spsEndde;
    private Date rnk1Bgnde;
    private Date rnk2Endde;


    /** ApplyHomeDTO → BoardDetailDTO 매핑 */
    public static BoardDetailDTO of(ApplyHomeDTO dto) {
        return BoardDetailDTO.builder()
                .NOTICE_ID(dto.getPBLANC_NO())
                .title(dto.getHOUSE_NM())
                .status(null)                      // dto에 없으면 필요에 따라 null 또는 기본값
                .bgnde(dto.getRCEPT_BGNDE())
                .endde(dto.getRCEPT_ENDDE())
                .address(dto.getHSSPLY_ADRES())
                .name(dto.getHOUSE_NM())
                .bsnsMby(dto.getBSNS_MBY_NM())
                .cnstrctEntr(dto.getCNSTRCT_ENTRPS_NM())
                .type(null)                        // 구분 로직 필요 시 추가
                .downPayment(null)                 // 공급금액이 dto에 없으면 매핑 대상에서 제외
                .bookmarkCount(null)
                .supplyAr(null)
                .totalHouseholds(String.valueOf(dto.getTOT_SUPLY_HSHLDCO()))
                .articleUrl(dto.getPBLANC_URL())
                .facilities(null)
                .contact(dto.getMDHS_TELNO())
                .compett(null)
                .imgUrl(null)
                .comments(null)
                .summ(null)
                .prearngeYm(parseDate(dto.getMVN_PREARNGE_YM()))   // YYYYMM → Date 변환 유틸 필요
                .presntnDe(parseDate(dto.getPRZWNER_PRESNATN_DE()))
                .rdnEarth(dto.getSPECLT_RDN_EARTH_AT())
                .spsBgnde(parseDate(dto.getSPSPLY_RCEPT_BGNDE()))
                .spsEndde(parseDate(dto.getSPSPLY_RCEPT_ENDDE()))
                .rnk1Bgnde(parseDate(dto.getGNRL_RNK1_CRSPAREA_RCPTDE()))
                .rnk2Endde(parseDate(dto.getGNRL_RNK2_CRSPAREA_RCPTDE()))
                .build();
    }

    private static Date parseDate(String s) {
        // YYYY-MM-DD 또는 YYYYMM 형태를 Date 로 파싱하는 유틸 (SimpleDateFormat 등)
        return null;
    }
}
