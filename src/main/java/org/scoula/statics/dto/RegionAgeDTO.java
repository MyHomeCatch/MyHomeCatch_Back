package org.scoula.statics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.statics.domain.ApplicantRegionVO;
import org.scoula.statics.domain.WinnerRateVO;
import org.scoula.statics.domain.WinnerRegionVO;

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
    public static RegionAgeDTO of(ApplicantRegionVO applicantVo, WinnerRegionVO winnerVo) {
        return (applicantVo == null || winnerVo == null) ? null : RegionAgeDTO.builder()
                .regionId(applicantVo.getRegionId())
                .statDe(applicantVo.getStatDe())
                .age30(applicantVo.getAge30())
                .age30Win(winnerVo.getAge30Win())
                .age40(applicantVo.getAge40())
                .age40Win(winnerVo.getAge40Win())
                .age50(applicantVo.getAge50())
                .age50Win(winnerVo.getAge50Win())
                .age60(applicantVo.getAge60())
                .age60Win(winnerVo.getAge60Win())
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

    public ApplicantRegionVO toApplicantVo() {
        return ApplicantRegionVO.builder()
                .regionId(regionId)
                .statDe(statDe)
                .age30(age30)
                .age40(age40)
                .age50(age50)
                .age60(age60)
                .build();
    }

    public WinnerRegionVO toWinnerVo() {
        return WinnerRegionVO.builder()
                .regionId(regionId)
                .statDe(statDe)
                .age30Win(age30Win)
                .age40Win(age40Win)
                .age50Win(age50Win)
                .age60Win(age60Win)
                .build();
    }
}
