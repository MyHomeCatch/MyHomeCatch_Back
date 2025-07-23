package org.scoula.lh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhHousingAttVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LH 주택 단지 첨부파일 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingAttDTO {

    /**
     * 주택첨부파일 ID (Primary Key, Auto Increment)
     */
    private Integer lhHousingAttId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 단지명
     * 예시: "고양장항 S-1", "서울강남권도시형주택(석촌)" 등
     */
    private String bzdtNm;

    /**
     * 파일구분명
     * 예시: "지역위치도", "단지조감도", "단지배치도", "토지이용계획도", "동호배치도" 등
     */
    private String slPanAhflDsCdNm;

    /**
     * 첨부파일명
     * 예시: "고양장항S-1블록_지역조감도_고.jpg", "고양장항S-1블록_단지배치도_고.jpg" 등
     */
    private String cmnAhflNm;

    /**
     * URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhImageView2.do?fileid=62568304"
     */
    private String ahflUrl;

    /**
     * LhHousingAttVO를 LhHousingAttDTO로 변환
     * @param vo LhHousingAttVO 객체
     * @return LhHousingAttDTO 객체
     */
    public static LhHousingAttDTO of(LhHousingAttVO vo) {
        return LhHousingAttDTO.builder()
                .lhHousingAttId(vo.getLhHousingAttId())
                .panId(vo.getPanId())
                .bzdtNm(vo.getBzdtNm())
                .slPanAhflDsCdNm(vo.getSlPanAhflDsCdNm())
                .cmnAhflNm(vo.getCmnAhflNm())
                .ahflUrl(vo.getAhflUrl())
                .build();
    }

    /**
     * LhHousingAttVO 리스트를 LhHousingAttDTO 리스트로 변환
     * @param voList LhHousingAttVO 리스트
     * @return LhHousingAttDTO 리스트
     */
    public static List<LhHousingAttDTO> of(List<LhHousingAttVO> voList) {
        return voList.stream()
                .map(LhHousingAttDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * LhHousingAttDTO를 LhHousingAttVO로 변환
     * @return LhHousingAttVO 객체
     */
    public LhHousingAttVO toVO() {
        return LhHousingAttVO.builder()
                .lhHousingAttId(this.lhHousingAttId)
                .panId(this.panId)
                .bzdtNm(this.bzdtNm)
                .slPanAhflDsCdNm(this.slPanAhflDsCdNm)
                .cmnAhflNm(this.cmnAhflNm)
                .ahflUrl(this.ahflUrl)
                .build();
    }
}