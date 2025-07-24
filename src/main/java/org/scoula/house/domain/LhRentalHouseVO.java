package org.scoula.house.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.domain.LhNoticeVO;
import org.scoula.lh.domain.NoticeAttVO;
import org.scoula.lh.domain.rental.LhRentalApplyVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhRentalHouseVO {
    // 임대주택 기본 정보
    private Integer lhRentalId;
    private String panId;
    private String lccNtNm;        // 단지명
    private String lgdnAdr;        // 단지주소
    private String lgdnDtlAdr;     // 단지상세주소
    private String ddoAr;          // 전용면적
    private String hshCnt;         // 총세대수
    private String htnFmlaDesc;    // 난방방식
    private String mvinXpcYm;      // 입주예정월


    // 관련 객체들
    private LhNoticeVO notice;                    // 공고 정보 (1:1)
    private List<NoticeAttVO> noticeAttList;      // 공고 첨부파일 (1:N)
    private LhRentalApplyVO apply;               // 신청 정보 (1:1)
    private List<LhRentalAttVO> lhRentalAttList;  // 첨부파일 목록 (1:N)
}
