package org.scoula.selfCheck.service;

import org.scoula.selfCheck.dto.SelfCheckContentDto;
import org.scoula.selfCheck.dto.SelfCheckRequestDto;

import java.util.Map;

public interface SelfCheckService {

    Map<String, Object> evaluateKookMin(SelfCheckRequestDto dto, int userId);

    Map<String, Object> evaluateHengBok(SelfCheckRequestDto dto, int userId);

    Map<String, Object> evaluateGongGong(SelfCheckRequestDto dto, int userId);

    Map<String, Object> evaluate09(SelfCheckRequestDto dto, int userId);

    void saveSelfCheckContent(SelfCheckRequestDto dto, int userId);

    void deleteSelfCheckContent(int userId);

    SelfCheckContentDto getSelfCheckContent(int userId);
}
