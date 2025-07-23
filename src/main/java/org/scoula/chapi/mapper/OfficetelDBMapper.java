package org.scoula.chapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scoula.chapi.domain.CHOfficetelCmpetVO;
import org.scoula.chapi.domain.CHOfficetelModelVO;
import org.scoula.chapi.domain.CHOfficetelTotalVO;
import org.scoula.chapi.domain.CHOfficetelVO;

import java.util.List;

@Mapper
public interface OfficetelDBMapper {
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

