package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.dto.lhHousing.LhHousingApplyDTO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhHousingDetailDTO {
    public NoticeDTO notice;
    public List<LhHousingApplyDTO> applyList;
    public List<NoticeAttDTO> noticeAttList;
    public List<LhHousingAttDTO> lhHousingAttList;
}
