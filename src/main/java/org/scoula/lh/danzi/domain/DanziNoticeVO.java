package org.scoula.lh.danzi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanziNoticeVO {

    private Integer id;                   // ID (PK, AUTO_INCREMENT)
    private Integer noticeId;             // 공고 ID (FK)
    private Integer danziId;              // 단지 ID (FK)
}
