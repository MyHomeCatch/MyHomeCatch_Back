package org.scoula.chapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.domain.CHOfficetelTotalVO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelTotalDTO {
    private CHOfficetelDTO basic;
    private List<CHOfficetelModelDTO> models;
    private List<CHOfficetelCmpetDTO> cmpets;

    public static CHOfficetelTotalDTO of(CHOfficetelTotalVO vo){
        if(vo == null) return null;
        return CHOfficetelTotalDTO.builder()
                .basic(CHOfficetelDTO.of(vo.getBasic()))
                .models(vo.getModels() != null ? vo.getModels().stream().map(CHOfficetelModelDTO::of).toList() : null)
                .cmpets(vo.getCmpets() != null ? vo.getCmpets().stream().map(CHOfficetelCmpetDTO::of).toList() : null)
                .build();
    }
    public CHOfficetelTotalVO toVO(){
        if(basic == null) return null;
        return CHOfficetelTotalVO.builder()
                .basic(this.basic.toVO())
                .models(this.models != null ? this.models.stream().map(CHOfficetelModelDTO::toVO).toList() : null)
                .cmpets(this.cmpets != null ? this.cmpets.stream().map(CHOfficetelCmpetDTO::toVO).toList() : null)
                .build();
    }
}
