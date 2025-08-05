package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.ThumbVO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThumbDTO {
   public String panId;
   // housing = bzdt_nm, rental = loc_nt_nm
   public String district;
   // 파일구분코드이름: 단지조감도, 위치도 지역조감도 단지배치도 동호배치도
   public String flDsCdNm;
   public String imgPath;

   public static ThumbVO toVO(ThumbDTO dto) {
      return dto == null ? null : ThumbVO.builder()
              .panId(dto.getPanId())
              .district(dto.getDistrict())
              .flDsCdNm(dto.getFlDsCdNm())
              .imgPath(dto.getImgPath())
              .build();
   }

   public static ThumbDTO toDTO(ThumbVO vo) {
      if (vo == null) {
         return null;
      }
      return ThumbDTO.builder()
              .panId(vo.panId())
              .district(vo.district())
              .flDsCdNm(vo.flDsCdNm())
              .imgPath(vo.imgPath())
              .build();
   }
}
