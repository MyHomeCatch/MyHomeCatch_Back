package org.scoula.house.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.applyHome.dto.ApplyHomeDTO;
import org.scoula.applyHome.dto.ApplyHomeModelDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ApplyHomeAptDetailDTO {
    // 공고 상세 정보
    private String houseManageNo; // 주택관리번호 = 공고번호
    private String pblancNo; // 공고번호
    private String mDatTrgetAreaSecd; // 조정대상지역 - 1순위 자격 강화, 대출규제
    private String hmpgAdres; //분양건물 홈피주소
    private String specltRdnEarthAt; // 투기과열지구
    private String lrsclBldlndAt; // 대규모택지개발지구 - 해당지역우선 공급비율 조정
    private String nplnPrvorpPublicHouseAt; //수도권내민영공공주택지구 - 전매제한 등
    private String publicHouseEarthAt; // 공공주택지구 건설 예정 여부
    private String publicHouseSpclwApplcAt;// 특별법: 우선공급 여부
    private String mdhsTelno; //문의처
    private Date spsplyRceptBgnde; // 특 접수시작
    private Date spsplyRceptEgnde; // 특 접수종료
    private Date gnrlRnk1CrspareaEndde; //1순위 해당지역 접수종료
    private Date gnrlRnk1CrspareaRcptde;
    private Date gnrlRnk1EtcAreaEndde;
    private Date gnrlRnk1EtcAreaRcptde;
    private Date gnrlRnk1EtcGgEndde; // 1순위 - 경기지역(기타 경기도) 접수 종료일
    private Date gnrlRnk1EtcGgRcptde;
    private Date gnrlRnk2CrspareaEndde;
    private Date gnrlRnk2CrspareaRcptde;
    private Date gnrlRnk2EtcAreaEndde;
    private Date gnrlRnk2EtcAreaRcptde;
    private Date gnrlRnk2EtcGgEndde;
    private Date gnrlRnk2EtcGgRcptde;

    // 모델별 달라지는 정보
    List<ApplyHomeModelDTO> models; // 공고-모델번호로 네이밍

    // 구현해야함
    private List<String> facilities;
    private Integer bookmarkCount;
    private int suplyHshldcoTotal; // 일반 공급세대수
    private int spsplyHshldcoTotal; // 특별 총공급세대수
    private int reqCntTotal; // 총 지원수
    private String amountRange; // (옵션) 공급금액 범위
    private String areaRange; // (옵션) 면적 범위

}
