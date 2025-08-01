package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanziAttDTO {
    int danziId;
    String fileTypeName;
    String fileName;
    String downloadUrl;

    public static DanziAttVO toDanziAttVO(DanziAttDTO dto) {
        return dto==null ? null: DanziAttVO.builder()
                .danziId(dto.getDanziId())
                .flDsCdNm(dto.getFileTypeName())
                .cmnAhflNm(dto.getFileName())
                .ahflUrl(dto.getDownloadUrl())
                .build();
    }

    public static DanziAttDTO toDanziAttDTO(DanziAttVO vo) {
        return vo==null?null: DanziAttDTO.builder()
                .danziId(vo.getDanziId())
                .fileName(vo.getCmnAhflNm())
                .fileTypeName(vo.getFlDsCdNm())
                .downloadUrl(vo.getAhflUrl())
                .build();

    }

    public static DanziAttVO ofHousing(LhHousingAttVO vo, int danzi_id) {
        LhHousingAttDTO dto = LhHousingAttDTO.of(vo);
        return DanziAttVO.builder()
//                .panId(dto.getPanId())
                .danziId(danzi_id)
                .flDsCdNm(dto.getSlPanAhflDsCdNm())
                .ahflUrl(dto.getAhflUrl())
                .build();
    }

    public static DanziAttVO ofRental(LhRentalAttVO vo, int danzi_id) {
        LhRentalAttDTO dto = LhRentalAttDTO.of(vo);
        return DanziAttVO.builder()
                .danziId(danzi_id)
                .cmnAhflNm(dto.getFileName())
                .flDsCdNm(dto.getFileTypeName())
                .ahflUrl(dto.getDownloadUrl())
                .build();
    }

    // rental, housing 이랑 단지 아이디 넣으면 DanziAttVO로 변환
    // 저장할때나, panID 특정 단지 att 정보 업뎃할 때
    public DanziAttVO convertToDanziAttVO(Object vo, int danziId) {
        if (vo instanceof LhHousingAttVO housingVO) {
            return DanziAttDTO.ofHousing(housingVO, danziId);
        } else if (vo instanceof LhRentalAttVO rentalVO) {
            return DanziAttDTO.ofRental(rentalVO, danziId);
        }
        throw new IllegalArgumentException("지원하지 않는 타입입니다: " + vo.getClass());
    }

}
