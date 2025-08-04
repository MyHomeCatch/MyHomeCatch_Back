package org.scoula.house.dto.HousePage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.HouseCardVO;
import org.scoula.house.util.RegionMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseCardDTO {

    private String danziId;
    private String houseName;           // bzdt_nm
    private String address;             // lct_ara_adr
    private Integer totalHouseCount;    // sum_tot_hsh_cnt
    private String exclusiveArea;       // min_max_rsdn_ddo_ar
    private String noticeType;          // ais_tp_cd_nm
    private String noticeStatus;        // pan_ss
    private String region;              // cnp_cd_nm
    private Date noticeStartDate; // pan_nt_st_dt
    private String overviewImageUrl;    // ahfl_url

    /**
     * HousingListVo를 HouseResponseDTO로 변환
     */
    public static HouseCardDTO ofHouseCardVO(HouseCardVO vo) {
        if (vo == null) {
            return null;
        }

        return HouseCardDTO.builder()
                .danziId(vo.getDanziId())
                .houseName(vo.getBzdtNm())
                .address(vo.getLctAraAdr())
                .totalHouseCount(vo.getSumTotHshCnt())
                .exclusiveArea(vo.getMinMaxRsdnDdoAr())
                .noticeType(vo.getAisTpCdNm())
                .noticeStatus(vo.getPanSs())
                .region(RegionMapper.mapToShortRegion(vo.getCnpCdNm()))
                .noticeStartDate(vo.getPanNtStDt() != null ? vo.getPanNtStDt() : null)
                .overviewImageUrl(vo.getAhflUrl())
                .build();
    }
}
