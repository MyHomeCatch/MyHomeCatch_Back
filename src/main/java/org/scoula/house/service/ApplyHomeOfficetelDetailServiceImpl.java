package org.scoula.house.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.house.dto.ApplyHomeOfficetelDetailDTO;
import org.scoula.house.mapper.ApplyHomeOfficetelDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApplyHomeOfficetelDetailServiceImpl implements ApplyHomeOfficetelDetailService {
    final ApplyHomeOfficetelDetailMapper mapper;

    @Override
    public List<ApplyHomeOfficetelDetailDTO> getApplyHomeOfficetelDetail(String pblancNo){
        return mapper.getApplyHomeOfficetelDetail(pblancNo);
    };
}
