package org.scoula.lh.dto.lhRental;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.rental.LhRentalAttVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LhRentalAttDTO {
    private Integer lhRentalAttId;    // 임대주택첨부파일ID
    private String panId;             // 공고ID
    private String houseName;           // 단지명
    private String fileTypeName; // 파일구분명
    private String fileName;         // 첨부파일명
    private String downloadUrl;           // url

    /**
     * DTO를 VO로 변환
     * @return LHRentalAttVO
     */
    public LhRentalAttVO toVO() {
        return LhRentalAttVO.builder()
                .lhRentalAttId(this.lhRentalAttId)
                .panId(this.panId)
                .locNtNm(this.houseName)
                .lsSplInfUplFlDsCdNm(this.fileTypeName)
                .cmnAhflNm(this.fileName)
                .ahflUrl(this.downloadUrl)
                .build();
    }

    /**
     * VO를 DTO로 변환
     * @param vo LHRentalAttVO
     * @return LHRentalAttDTO
     */
    public static LhRentalAttDTO of(LhRentalAttVO vo) {
        return LhRentalAttDTO.builder()
                .lhRentalAttId(vo.getLhRentalAttId())
                .panId(vo.getPanId())
                .houseName(vo.getLocNtNm())
                .fileTypeName(vo.getLsSplInfUplFlDsCdNm())
                .fileName(vo.getCmnAhflNm())
                .downloadUrl(vo.getAhflUrl())
                .build();
    }
}