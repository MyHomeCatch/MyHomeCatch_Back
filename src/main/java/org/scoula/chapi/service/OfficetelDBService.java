package org.scoula.chapi.service;

import org.scoula.chapi.dto.CHOfficetelCmpetDTO;
import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.dto.CHOfficetelModelDTO;
import org.scoula.chapi.dto.CHOfficetelTotalDTO;

import java.util.List;

public interface OfficetelDBService {
    // applyHome Officetel Data
    int insert(CHOfficetelDTO dto);
    int insertAll(List<CHOfficetelDTO> dtoList);
    int fetchAndInsertAPIData();

    // applyHome Officetel Model Data
    int insertOfficetelModel(CHOfficetelModelDTO dto);
    int insertAllOfficetelModel(List<CHOfficetelModelDTO> dtoList);
    int fetchOfficetelModel();

    // applyHome Officetel Compe
    int insertOfficetelCmpet(CHOfficetelCmpetDTO dto);
    int insertAllOfficetelCmpet(List<CHOfficetelCmpetDTO> dtoList);
    int fetchOfficetelCmpet();

    // GET Methods
    CHOfficetelDTO getOfficetelBasic(String pblancNo);
    List<CHOfficetelModelDTO> getOfficetelModels(String pblancNo);
    List<CHOfficetelCmpetDTO> getOfficetelCmpets(String pblancNo);
    // JOIN GET Method
    CHOfficetelTotalDTO getOfficetelTotal(String pblancNo);




}
