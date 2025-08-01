package org.scoula.lh.dto.rentalNoticeDetailApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeAttVO;

/**
 * 임대주택 첨부파일 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDsAhflInfoDTO {

    /**
     * 첨부파일 URL
     * 예시: "https://apply.lh.or.kr/lhapply/lhFile.do?fileid=62373671"
     */
    @JsonProperty("AHFL_URL")
    private String ahflUrl;

    /**
     * 파일구분명
     * 예시: "기타 첨부파일", "카탈로그", "공고문(hwp)", "공고문(PDF)" 등
     */
    @JsonProperty("SL_PAN_AHFL_DS_CD_NM")
    private String slPanAhflDsCdNm;

    /**
     * 첨부파일명
     * 예시: "(참고)마감재시설내역(안).pdf", "(팸플릿)6-3M중1행복주택팸플릿.pdf" 등
     */
    @JsonProperty("CMN_AHFL_NM")
    private String cmnAhflNm;


    public NoticeAttVO toNoticeAttVO(Integer noticeId) {
        return NoticeAttVO.builder()
                .noticeId(noticeId)
                .slPanAhflDsCdNm(slPanAhflDsCdNm)
                .cmnAhflNm(cmnAhflNm)
                .ahflUrl(ahflUrl)
                .build();
    }
}