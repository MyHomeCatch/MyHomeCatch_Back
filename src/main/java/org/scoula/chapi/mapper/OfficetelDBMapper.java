package org.scoula.chapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.chapi.domain.*;

import java.util.List;

@Mapper
public interface OfficetelDBMapper {
    // 오피스텔
    int insert(CHOfficetelVO vo);
    int insertOfficetelModel(CHOfficetelModelVO vo);
    int insertOfficetelCmpet(CHOfficetelCmpetVO vo);

    // GET
    CHOfficetelVO getOfficetelBasic(String pblancNo);
    List<CHOfficetelModelVO> getOfficetelModels(String pblancNo);
    List<CHOfficetelCmpetVO> getOfficetelCmpets(String pblancNo);
    // GET TOTAL DATA
    CHOfficetelTotalVO getOfficetelTotal(String pblancNo);
}

