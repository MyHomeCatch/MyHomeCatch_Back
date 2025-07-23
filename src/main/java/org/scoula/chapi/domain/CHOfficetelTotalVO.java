package org.scoula.chapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelTotalVO {
    private CHOfficetelVO basic;
    private List<CHOfficetelModelVO> models;
    private List<CHOfficetelCmpetVO> cmpets;
}
