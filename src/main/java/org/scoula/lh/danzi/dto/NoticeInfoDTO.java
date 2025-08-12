package org.scoula.lh.danzi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.lh.danzi.domain.NoticeVO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeInfoDTO {
    private Integer noticeId;
    private String panId;
    private String uppAisTpCd;
    private String aisTpCdNm;
    private String panNm;
    private String cnpCdNm;
    private String panSs;
    private String allCnt;
    private Date panNtStDt;
    private String dtlUrl;

    private String splInfTpCd;
    private String ccrCnntSysDsCd;
    private String aisTpCd;

    // NoticeAttVO 정보 (1:N 관계)
    private List<NoticeAttDTO> noticeAttachments;

    public static NoticeInfoDTO from(NoticeVO vo, List<NoticeAttDTO> attachments) {
        return NoticeInfoDTO.builder()
                .noticeId(vo.getNoticeId())
                .panId(vo.getPanId())
                .uppAisTpCd(vo.getUppAisTpCd())
                .aisTpCdNm(vo.getAisTpCdNm())
                .panNm(vo.getPanNm())
                .cnpCdNm(vo.getCnpCdNm())
                .panSs(vo.getPanSs())
                .allCnt(vo.getAllCnt())
                .panNtStDt(vo.getPanNtStDt())
                .dtlUrl(vo.getDtlUrl())
                .splInfTpCd(vo.getSplInfTpCd())
                .ccrCnntSysDsCd(vo.getCcrCnntSysDsCd())
                .aisTpCd(vo.getAisTpCd())
                .noticeAttachments(attachments)
                .build();
    }
}
