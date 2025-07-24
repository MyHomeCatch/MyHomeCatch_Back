package org.scoula.house.service;

import org.scoula.house.dto.ApplyHomeOfficetelDetailDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ApplyHomeOfficetelDetailService {
    // GET
    List<ApplyHomeOfficetelDetailDTO> getApplyHomeOfficetelDetail(String pblancNo);
}
