package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.dto.NoticeAttDTO;
import org.scoula.lh.dto.NoticeDTO;
import org.scoula.lh.dto.lhRental.LhRentalApplyDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalDetailDTO {
    public NoticeDTO notice;
    public LhRentalApplyDTO apply;
    public List<NoticeAttDTO> noticeAtts;
    public List<LhRentalAttDTO> lhRentalAtts;

}
