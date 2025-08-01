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
    private Integer noticeId;
    /**
     * 첨부파일 ID (Primary Key, Auto Increment)
     */
    private Integer lhNoticeAttId;

    /**
     * 공고 ID (Foreign Key - LH_notice.pan_id 참조)
     */
//    private String panId;

    /**
     * 파일구분명
     * 예시: "공고문(PDF)", "기타 첨부파일", "정정공고문(HWP)" 등
     */
    private String fileTypeName;

    /**
     * 첨부파일명
     * 예시: "고양장항_S-1블록_팸플릿.pdf", "위임장.hwp" 등
     */
    private String fileName;

    /**
     * 다운로드 URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhFile.do?fileid=62586087"
     */
    private String downloadUrl;


    /**
     * NoticeAttVO를 NoticeAttDTO로 변환
     * @param vo NoticeAttVO 객체
     * @return NoticeAttDTO 객체
     */
    public static NoticeAttDTO of(NoticeAttVO vo) {
        return NoticeAttDTO.builder()
                .noticeId(vo.getNoticeId())
                .lhNoticeAttId(vo.getLhNoticeAttId())
//                .panId(vo.getPanId())
                .fileTypeName(vo.getSlPanAhflDsCdNm())
                .fileName(vo.getCmnAhflNm())
                .downloadUrl(vo.getAhflUrl())
                .build();
    }
}
