package org.scoula.chapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.chapi.dto.ApplyHomePublicPrivateRentDTO;
import org.scoula.chapi.mapper.PublicPrivateRentDBMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PublicPrivateRentDBSerivceImpl implements PublicPrivateRentDBService {
    final PublicPrivateRentDBMapper mapper;

    @Override
    public int insert(ApplyHomePublicPrivateRentDTO dto){
        return mapper.insert(dto.toVO());
    }
}
