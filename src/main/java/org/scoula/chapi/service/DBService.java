package org.scoula.chapi.service;

import org.scoula.chapi.dto.CHOfficetelDTO;

import java.util.List;

public interface DBService {
    CHOfficetelDTO insert(CHOfficetelDTO dto);

    List<CHOfficetelDTO> insertAll(List<CHOfficetelDTO> dtoList);

}
