package org.scoula.chapi.service;

import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.mapper.DBMapper;
import org.springframework.transaction.annotation.Transactional;

public class DBServiceImpl implements DBService {
    final DBMapper mapper;

    @Transactional
    @Override
    public CHOfficetelDTO insert(CHOfficetelDTO dto) {}
}
