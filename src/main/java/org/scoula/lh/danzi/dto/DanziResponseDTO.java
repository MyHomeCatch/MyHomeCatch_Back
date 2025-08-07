package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanziResponseDTO {
    private DanziDTO danzi;
    // DanziApplyVO 정보 (1:N 관계)
    private List<DanziApplyDTO> applies;
    // DanziAttVO 정보 (1:N 관계)
    private List<DanziAttDTO> attachments;
    // NoticeVO + NoticeAttVO 정보 (1:N 관계)
    private List<NoticeInfoDTO> notices;


}
