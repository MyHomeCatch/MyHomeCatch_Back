package org.scoula.lh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.NoticeAttVO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeAttDTO {
    /**
     * 첨부파일 ID (Primary Key, Auto Increment)
     */
    private Integer noticeAttId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
    private String panId;

    /**
     * 파일구분명
     * 예시: "공고문(PDF)", "기타 첨부파일", "정정공고문(HWP)" 등
     */
    private String slPanAhflDsCdNm;

    /**
     * 첨부파일명
     * 예시: "고양장항_S-1블록_팸플릿.pdf", "위임장.hwp" 등
     */
    private String cmnAhflNm;

    /**
     * 다운로드 URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhFile.do?fileid=62586087"
     */
    private String ahflUrl;


    /**
     * NoticeAttVO를 NoticeAttDTO로 변환
     * @param vo NoticeAttVO 객체
     * @return NoticeAttDTO 객체
     */
    public static NoticeAttDTO of(NoticeAttVO vo) {
        return NoticeAttDTO.builder()
                .noticeAttId(vo.getNoticeAttId())
                .panId(vo.getPanId())
                .slPanAhflDsCdNm(vo.getSlPanAhflDsCdNm())
                .cmnAhflNm(vo.getCmnAhflNm())
                .ahflUrl(vo.getAhflUrl())
                .build();
    }
}
