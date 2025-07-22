package org.scoula.applyHome.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.applyHome.dto.ApplyHomeDTO;
import org.scoula.applyHome.mapper.ApplyHomeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApplyHomeServiceImpl implements ApplyHomeService {

    final private ApplyHomeMapper mapper;

    @Override
    public int create(ApplyHomeDTO dto) {
        int row = mapper.create(dto);
        return row;
    }

    @Override
    public ApplyHomeDTO getApplyHome(String NOTICE_ID) {
        log.info("get ApplyHomeDTO....");
        ApplyHomeDTO applyhome = ApplyHomeDTO.of(mapper.get(NOTICE_ID));
        return Optional.ofNullable(applyhome).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<ApplyHomeDTO> getApplyHomeList() {
        log.info("get ApplyHome List....");
        return mapper.getList().stream().map(ApplyHomeDTO::of).toList();
    }
}
