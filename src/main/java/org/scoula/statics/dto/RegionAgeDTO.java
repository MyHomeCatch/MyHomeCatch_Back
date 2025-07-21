package org.scoula.statics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.statics.domain.WinnerRateVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionAgeDTO {

    private Long regionId;
    private String statDe;
    private int age30;
    private int age30Win;
    private int age40;
    private int age40Win;
    private int age50;
    private int age50Win;
    private int age60;
    private int age60Win;


    // VO DTO변환
    public static RegionAgeDTO of(WinnerRateVO vo) {
        return vo == null ? null : RegionAgeDTO.builder()
                .regionId(vo.getRegionId())
                .statDe(vo.getStatDe())
                .age30(vo.getAge30())
                .age30Win(vo.getAge30Win())
                .age40(vo.getAge40())
                .age40Win(vo.getAge40Win())
                .age50(vo.getAge50())
                .age50Win(vo.getAge50Win())
                .age60(vo.getAge60())
                .age60Win(vo.getAge60Win())
                .build();
    }

    // DTO VO변환
    public WinnerRateVO toVo() {
        return WinnerRateVO.builder()
                .regionId(regionId)
                .age30(age30)
                .age30Win(age30Win)
                .age40(age40)
                .age40Win(age40Win)
                .age50(age50)
                .age50Win(age50Win)
                .age60(age60)
                .age60Win(age60Win)
                .build();
    }
}
