package org.scoula.house.mapper;

import org.scoula.house.domain.ApplyHomeAptPublicVO;

import java.util.List;

public interface ApplyHomeAPTMapper {
    // 2025년도 데이터만 가져오도록
    // 분양에서도 행복주택, 공공임대 등 나뉘는 것 같은데 직접 추출해서 카테고리로 만들어야 함
    List<ApplyHomeAptPublicVO> getAll();
    ApplyHomeAptPublicVO getByHouseNo(String houseNo);
}
