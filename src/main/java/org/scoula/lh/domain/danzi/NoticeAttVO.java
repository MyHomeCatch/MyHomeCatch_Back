package org.scoula.lh.domain.danzi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeAttVO {

    private Integer noticeAttId;          // notice_att ID (PK, AUTO_INCREMENT)
    private Integer noticeId;                // notice ID (FK)
    private String slPanAhflDsCdNm;       // 파일구분명
    private String cmnAhflNm;             // 첨부파일명
    private String ahflUrl;               // 다운로드 URL

}