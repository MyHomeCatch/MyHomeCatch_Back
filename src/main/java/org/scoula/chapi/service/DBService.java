package org.scoula.chapi.service;

import org.scoula.chapi.dto.CHOfficetelDTO;

import java.util.List;

public interface DBService {
    int insert(CHOfficetelDTO dto);

    int insertAll(List<CHOfficetelDTO> dtoList);

    int fetchAndInsertAPIData();

}
