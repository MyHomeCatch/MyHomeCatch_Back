package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeAttVO;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttDTO {
    private Integer noticeAttId;
    private Integer noticeId;
    private String slPanAhflDsCdNm;
    private String cmnAhflNm;
    private String ahflUrl;

    public static NoticeAttDTO from(NoticeAttVO vo) {
        return NoticeAttDTO.builder()
                .noticeAttId(vo.getNoticeAttId())
                .noticeId(vo.getNoticeId())
                .slPanAhflDsCdNm(vo.getSlPanAhflDsCdNm())
                .cmnAhflNm(vo.getCmnAhflNm())
                .ahflUrl(vo.getAhflUrl())
                .build();
    }

}
