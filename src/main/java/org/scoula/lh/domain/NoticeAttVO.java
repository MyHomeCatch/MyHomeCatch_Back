package org.scoula.lh.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeAttVO {

    /**
     * 첨부파일 ID (Primary Key, Auto Increment)
     */
    private Integer lhNoticeAttId;

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
}