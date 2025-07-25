package org.scoula.applyHome.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.applyHome.domain.ApplyHomeVO;
//import org.scoula.dto.BoardDetailDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "청년홈 APT DTO")
public class ApplyHomeDTO {
    private String PBLANC_NO;
    private String BSNS_MBY_NM;
    private String CNSTRCT_ENTRPS_NM;
    private String CNTRCT_CNCLS_BGNDE;
    private String CNTRCT_CNCLS_ENDDE;
    private String GNRL_RNK1_CRSPAREA_ENDDE;
    private String GNRL_RNK1_CRSPAREA_RCPTDE;
    private String GNRL_RNK1_ETC_AREA_ENDDE;
    private String GNRL_RNK1_ETC_AREA_RCPTDE;
    private String GNRL_RNK1_ETC_GG_ENDDE;
    private String GNRL_RNK1_ETC_GG_RCPTDE;
    private String GNRL_RNK2_CRSPAREA_ENDDE;
    private String GNRL_RNK2_CRSPAREA_RCPTDE;
    private String GNRL_RNK2_ETC_AREA_ENDDE;
    private String GNRL_RNK2_ETC_AREA_RCPTDE;
    private String GNRL_RNK2_ETC_GG_ENDDE;
    private String GNRL_RNK2_ETC_GG_RCPTDE;
    private String HMPG_ADRES;
    private String HOUSE_DTL_SECD;
    private String HOUSE_DTL_SECD_NM;
    private String HOUSE_MANAGE_NO;
    private String HOUSE_NM;
    private String HOUSE_SECD;
    private String HOUSE_SECD_NM;
    private String HSSPLY_ADRES;
    private String HSSPLY_ZIP;
    private String IMPRMN_BSNS_AT;
    private String LRSCL_BLDLND_AT;
    private String MDAT_TRGET_AREA_SECD;
    private String MDHS_TELNO;
    private String MVN_PREARNGE_YM;
    private String NPLN_PRVOPR_PUBLIC_HOUSE_AT;
    private String NSPR_C_NM;
    private String PBLANC_URL;
    private String PRZWNER_PRESNATN_DE;
    private String PUBLIC_HOUSE_EARTH_AT;
    private String PUBLIC_HOUSE_SPCLW_APPLC_AT;
    private String RCEPT_BGNDE;
    private String RCEPT_ENDDE;
    private String RCRIT_PBLANC_DE;
    private String RENT_SECD;
    private String RENT_SECD_NM;
    private String SPECLT_RDN_EARTH_AT;
    private String SPSPLY_RCEPT_BGNDE;
    private String SPSPLY_RCEPT_ENDDE;
    private String SUBSCRPT_AREA_CODE;
    private String SUBSCRPT_AREA_CODE_NM;
    private int    TOT_SUPLY_HSHLDCO;


    // ApplyHomeDTO to BoardDetailDTO
//    public BoardDetailDTO toBoardDetailDTO() {
//        return BoardDetailDTO.of(this);
//    }


    // VO to DTO
    public static ApplyHomeDTO of(ApplyHomeVO vo){
        return vo == null ? null : ApplyHomeDTO.builder()
                .PBLANC_NO(vo.getPBLANC_NO())
                .BSNS_MBY_NM(vo.getBSNS_MBY_NM())
                .CNSTRCT_ENTRPS_NM(vo.getCNSTRCT_ENTRPS_NM())
                .CNTRCT_CNCLS_BGNDE(vo.getCNTRCT_CNCLS_BGNDE())
                .CNTRCT_CNCLS_ENDDE(vo.getCNTRCT_CNCLS_ENDDE())
                .GNRL_RNK1_CRSPAREA_ENDDE(vo.getGNRL_RNK1_CRSPAREA_ENDDE())
                .GNRL_RNK1_CRSPAREA_RCPTDE(vo.getGNRL_RNK1_CRSPAREA_RCPTDE())
                .GNRL_RNK1_ETC_AREA_ENDDE(vo.getGNRL_RNK1_ETC_AREA_ENDDE())
                .GNRL_RNK1_ETC_AREA_RCPTDE(vo.getGNRL_RNK1_ETC_AREA_RCPTDE())
                .GNRL_RNK1_ETC_GG_ENDDE(vo.getGNRL_RNK1_ETC_GG_ENDDE())
                .GNRL_RNK1_ETC_GG_RCPTDE(vo.getGNRL_RNK1_ETC_GG_RCPTDE())
                .GNRL_RNK2_CRSPAREA_ENDDE(vo.getGNRL_RNK2_CRSPAREA_ENDDE())
                .GNRL_RNK2_CRSPAREA_RCPTDE(vo.getGNRL_RNK2_CRSPAREA_RCPTDE())
                .GNRL_RNK2_ETC_AREA_ENDDE(vo.getGNRL_RNK2_ETC_AREA_ENDDE())
                .GNRL_RNK2_ETC_AREA_RCPTDE(vo.getGNRL_RNK2_ETC_AREA_RCPTDE())
                .GNRL_RNK2_ETC_GG_ENDDE(vo.getGNRL_RNK2_ETC_GG_ENDDE())
                .GNRL_RNK2_ETC_GG_RCPTDE(vo.getGNRL_RNK2_ETC_GG_RCPTDE())
                .HMPG_ADRES(vo.getHMPG_ADRES())
                .HOUSE_DTL_SECD(vo.getHOUSE_DTL_SECD())
                .HOUSE_DTL_SECD_NM(vo.getHOUSE_DTL_SECD_NM())
                .HOUSE_MANAGE_NO(vo.getHOUSE_MANAGE_NO())
                .HOUSE_NM(vo.getHOUSE_NM())
                .HOUSE_SECD(vo.getHOUSE_SECD())
                .HOUSE_SECD_NM(vo.getHOUSE_SECD_NM())
                .HSSPLY_ADRES(vo.getHSSPLY_ADRES())
                .HSSPLY_ZIP(vo.getHSSPLY_ZIP())
                .IMPRMN_BSNS_AT(vo.getIMPRMN_BSNS_AT())
                .LRSCL_BLDLND_AT(vo.getLRSCL_BLDLND_AT())
                .MDAT_TRGET_AREA_SECD(vo.getMDAT_TRGET_AREA_SECD())
                .MDHS_TELNO(vo.getMDHS_TELNO())
                .MVN_PREARNGE_YM(vo.getMVN_PREARNGE_YM())
                .NPLN_PRVOPR_PUBLIC_HOUSE_AT(vo.getNPLN_PRVOPR_PUBLIC_HOUSE_AT())
                .NSPR_C_NM(vo.getNSPR_C_NM())
                .PBLANC_URL(vo.getPBLANC_URL())
                .PRZWNER_PRESNATN_DE(vo.getPRZWNER_PRESNATN_DE())
                .PUBLIC_HOUSE_EARTH_AT(vo.getPUBLIC_HOUSE_EARTH_AT())
                .PUBLIC_HOUSE_SPCLW_APPLC_AT(vo.getPUBLIC_HOUSE_SPCLW_APPLC_AT())
                .RCEPT_BGNDE(vo.getRCEPT_BGNDE())
                .RCEPT_ENDDE(vo.getRCEPT_ENDDE())
                .RCRIT_PBLANC_DE(vo.getRCRIT_PBLANC_DE())
                .RENT_SECD(vo.getRENT_SECD())
                .RENT_SECD_NM(vo.getRENT_SECD_NM())
                .SPECLT_RDN_EARTH_AT(vo.getSPECLT_RDN_EARTH_AT())
                .SPSPLY_RCEPT_BGNDE(vo.getSPSPLY_RCEPT_BGNDE())
                .SPSPLY_RCEPT_ENDDE(vo.getSPSPLY_RCEPT_ENDDE())
                .SUBSCRPT_AREA_CODE(vo.getSUBSCRPT_AREA_CODE())
                .SUBSCRPT_AREA_CODE_NM(vo.getSUBSCRPT_AREA_CODE_NM())
                .TOT_SUPLY_HSHLDCO(vo.getTOT_SUPLY_HSHLDCO())
                .build();
    }


    // DTO to VO
    public static ApplyHomeVO toVO(ApplyHomeDTO dto) {
        return dto == null ? null : ApplyHomeVO.builder()
                .PBLANC_NO(dto.getPBLANC_NO())
                .BSNS_MBY_NM(dto.getBSNS_MBY_NM())
                .CNSTRCT_ENTRPS_NM(dto.getCNSTRCT_ENTRPS_NM())
                .CNTRCT_CNCLS_BGNDE(dto.getCNTRCT_CNCLS_BGNDE())
                .CNTRCT_CNCLS_ENDDE(dto.getCNTRCT_CNCLS_ENDDE())
                .GNRL_RNK1_CRSPAREA_ENDDE(dto.getGNRL_RNK1_CRSPAREA_ENDDE())
                .GNRL_RNK1_CRSPAREA_RCPTDE(dto.getGNRL_RNK1_CRSPAREA_RCPTDE())
                .GNRL_RNK1_ETC_AREA_ENDDE(dto.getGNRL_RNK1_ETC_AREA_ENDDE())
                .GNRL_RNK1_ETC_AREA_RCPTDE(dto.getGNRL_RNK1_ETC_AREA_RCPTDE())
                .GNRL_RNK1_ETC_GG_ENDDE(dto.getGNRL_RNK1_ETC_GG_ENDDE())
                .GNRL_RNK1_ETC_GG_RCPTDE(dto.getGNRL_RNK1_ETC_GG_RCPTDE())
                .GNRL_RNK2_CRSPAREA_ENDDE(dto.getGNRL_RNK2_CRSPAREA_ENDDE())
                .GNRL_RNK2_CRSPAREA_RCPTDE(dto.getGNRL_RNK2_CRSPAREA_RCPTDE())
                .GNRL_RNK2_ETC_AREA_ENDDE(dto.getGNRL_RNK2_ETC_AREA_ENDDE())
                .GNRL_RNK2_ETC_AREA_RCPTDE(dto.getGNRL_RNK2_ETC_AREA_RCPTDE())
                .GNRL_RNK2_ETC_GG_ENDDE(dto.getGNRL_RNK2_ETC_GG_ENDDE())
                .GNRL_RNK2_ETC_GG_RCPTDE(dto.getGNRL_RNK2_ETC_GG_RCPTDE())
                .HMPG_ADRES(dto.getHMPG_ADRES())
                .HOUSE_DTL_SECD(dto.getHOUSE_DTL_SECD())
                .HOUSE_DTL_SECD_NM(dto.getHOUSE_DTL_SECD_NM())
                .HOUSE_MANAGE_NO(dto.getHOUSE_MANAGE_NO())
                .HOUSE_NM(dto.getHOUSE_NM())
                .HOUSE_SECD(dto.getHOUSE_SECD())
                .HOUSE_SECD_NM(dto.getHOUSE_SECD_NM())
                .HSSPLY_ADRES(dto.getHSSPLY_ADRES())
                .HSSPLY_ZIP(dto.getHSSPLY_ZIP())
                .IMPRMN_BSNS_AT(dto.getIMPRMN_BSNS_AT())
                .LRSCL_BLDLND_AT(dto.getLRSCL_BLDLND_AT())
                .MDAT_TRGET_AREA_SECD(dto.getMDAT_TRGET_AREA_SECD())
                .MDHS_TELNO(dto.getMDHS_TELNO())
                .MVN_PREARNGE_YM(dto.getMVN_PREARNGE_YM())
                .NPLN_PRVOPR_PUBLIC_HOUSE_AT(dto.getNPLN_PRVOPR_PUBLIC_HOUSE_AT())
                .NSPR_C_NM(dto.getNSPR_C_NM())
                .PBLANC_URL(dto.getPBLANC_URL())
                .PRZWNER_PRESNATN_DE(dto.getPRZWNER_PRESNATN_DE())
                .PUBLIC_HOUSE_EARTH_AT(dto.getPUBLIC_HOUSE_EARTH_AT())
                .PUBLIC_HOUSE_SPCLW_APPLC_AT(dto.getPUBLIC_HOUSE_SPCLW_APPLC_AT())
                .RCEPT_BGNDE(dto.getRCEPT_BGNDE())
                .RCEPT_ENDDE(dto.getRCEPT_ENDDE())
                .RCRIT_PBLANC_DE(dto.getRCRIT_PBLANC_DE())
                .RENT_SECD(dto.getRENT_SECD())
                .RENT_SECD_NM(dto.getRENT_SECD_NM())
                .SPECLT_RDN_EARTH_AT(dto.getSPECLT_RDN_EARTH_AT())
                .SPSPLY_RCEPT_BGNDE(dto.getSPSPLY_RCEPT_BGNDE())
                .SPSPLY_RCEPT_ENDDE(dto.getSPSPLY_RCEPT_ENDDE())
                .SUBSCRPT_AREA_CODE(dto.getSUBSCRPT_AREA_CODE())
                .SUBSCRPT_AREA_CODE_NM(dto.getSUBSCRPT_AREA_CODE_NM())
                .TOT_SUPLY_HSHLDCO(dto.getTOT_SUPLY_HSHLDCO())
                .build();
    }

    public ApplyHomeVO toVO() {
        return ApplyHomeDTO.toVO(this);
    }
}
