package org.scoula.chapi.service;

import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.dto.CHOfficetelModelDTO;

import java.util.List;

public interface OfficetelDBService {
    int insert(CHOfficetelDTO dto);

    int insertAll(List<CHOfficetelDTO> dtoList);

    int fetchAndInsertAPIData();

    int insertOfficetelModel(CHOfficetelModelDTO dto);

    int insertAllOfficetelModel(List<CHOfficetelModelDTO> dtoList);

    int fetchOfficetelModel();

}
