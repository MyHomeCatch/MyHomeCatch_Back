package org.scoula.house.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.house.domain.ApplyHomeAptPublicVO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyHomeAptPublicDTO {
    // 공고 상세 정보
    private String houseManageNo; // 주택관리번호 = 공고번호
    private String pblancNo; // 공고번호
    private String bsnsMbyNm;
    private String cnstrctEntrpsNm;
    private String houseNm;
    private Date cntrctCnclsBgnde;
    private Date cntrctCnclsEndde;
    private String houseSecd;
    private String houseSecdNm;
    private String hssplyAdres;
    private String hssplyZip;
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
    private Integer totSuplyHshldco;
    private Date przwnerPresnatnDe;
    private String pblanUrl;
    private String mvnPrearngeYm; // 입주 예정 연월 (YYYYMM)
    /**
     * 임대 구분명
     */
    private String rentSecdNm;

    /**
     * 모집 공고일 ()
     */
    private Date rcritPblancDe;

    /**
     * 청약 지역 코드
     */
    private String subscrptAreaCode;

    /**
     * 청약 지역명
     */
    private String subscrptAreaCodeNm;


    // 모델별 달라지는 정보
    // 국민 공급은 모델별로 안달라짐
//    List<ApplyHomeModelDTO> models; // 공고-모델번호로 네이밍

    // 구현해야함
//    private List<String> facilities;
//    private Integer bookmarkCount;
//    private int suplyHshldcoTotal; // 일반 공급세대수
//    private int spsplyHshldcoTotal; // 특별 총공급세대수
//    private int reqCntTotal; // 총 지원수
//    private String amountRange; // (옵션) 공급금액 범위
//    private String areaRange; // (옵션) 면적 범위

    public static ApplyHomeAptPublicVO toVO(ApplyHomeAptPublicDTO dto) {
        if (dto == null) return null;
        return ApplyHomeAptPublicVO.builder()
                .pblancNo(dto.getPblancNo())
                .bsnsMbyNm(dto.getBsnsMbyNm())
                .cnstrctEntrpsNm(dto.getCnstrctEntrpsNm())
                .cntrctCnclsBgnde(dto.getCntrctCnclsBgnde())
                .cntrctCnclsEndde(dto.getCntrctCnclsEndde())
                .gnrlRnk1CrspareaEndde(dto.getGnrlRnk1CrspareaEndde())
                .gnrlRnk1CrspareaRcptde(dto.getGnrlRnk1CrspareaRcptde())
                .gnrlRnk1EtcAreaEndde(dto.getGnrlRnk1EtcAreaEndde())
                .gnrlRnk1EtcAreaRcptde(dto.getGnrlRnk1EtcAreaRcptde())
                .gnrlRnk1EtcGgEndde(dto.getGnrlRnk1EtcGgEndde())
                .gnrlRnk1EtcGgRcptde(dto.getGnrlRnk1EtcGgRcptde())
                .gnrlRnk2CrspareaEndde(dto.getGnrlRnk2CrspareaEndde())
                .gnrlRnk2CrspareaRcptde(dto.getGnrlRnk2CrspareaRcptde())
                .gnrlRnk2EtcAreaEndde(dto.getGnrlRnk2EtcAreaEndde())
                .gnrlRnk2EtcAreaRcptde(dto.getGnrlRnk2EtcAreaRcptde())
                .gnrlRnk2EtcGgEndde(dto.getGnrlRnk2EtcGgEndde())
                .gnrlRnk2EtcGgRcptde(dto.getGnrlRnk2EtcGgRcptde())
                .hmpgAdres(dto.getHmpgAdres())
                .houseManageNo(dto.getHouseManageNo())
                .houseNm(dto.getHouseNm())
                .houseSecd(dto.getHouseSecd())
                .houseSecdNm(dto.getHouseSecdNm())
                .hssplyAdres(dto.getHssplyAdres())
                .hssplyZip(dto.getHssplyZip())
                .mDatTrgetAreaSecd(dto.getMDatTrgetAreaSecd())
                .mdhsTelno(dto.getMdhsTelno())
                .mvnPrearngeYm((dto.getMvnPrearngeYm()))
                .nplnPrvorpPublicHouseAt(dto.getNplnPrvorpPublicHouseAt())
                .przwnerPresnatnDe(dto.getPrzwnerPresnatnDe())
                .publicHouseEarthAt(dto.getPublicHouseEarthAt())
                .publicHouseSpclwApplcAt(dto.getPublicHouseSpclwApplcAt())
                .rcritPblancDe(dto.getRcritPblancDe())
                .rentSecdNm(dto.getRentSecdNm())
                .specltRdnEarthAt(dto.getSpecltRdnEarthAt())
                .spsplyRceptBgnde(dto.getSpsplyRceptBgnde())
                .spsplyRceptEgnde(dto.getSpsplyRceptEgnde())
                .subscrptAreaCode(dto.getSubscrptAreaCode())
                .subscrptAreaCodeNm(dto.getSubscrptAreaCodeNm())
                .totSuplyHshldco(dto.getTotSuplyHshldco())
                .build();
    }

    public static ApplyHomeAptPublicDTO toDTO(ApplyHomeAptPublicVO vo) {
        if (vo == null) return null;
        return ApplyHomeAptPublicDTO.builder()
                .pblancNo(vo.getPblancNo())
                .bsnsMbyNm(vo.getBsnsMbyNm())
                .cnstrctEntrpsNm(vo.getCnstrctEntrpsNm())
                .cntrctCnclsBgnde(vo.getCntrctCnclsBgnde())
                .cntrctCnclsEndde(vo.getCntrctCnclsEndde())
                .gnrlRnk1CrspareaEndde(vo.getGnrlRnk1CrspareaEndde())
                .gnrlRnk1CrspareaRcptde(vo.getGnrlRnk1CrspareaRcptde())
                .gnrlRnk1EtcAreaEndde(vo.getGnrlRnk1EtcAreaEndde())
                .gnrlRnk1EtcAreaRcptde(vo.getGnrlRnk1EtcAreaRcptde())
                .gnrlRnk1EtcGgEndde(vo.getGnrlRnk1EtcGgEndde())
                .gnrlRnk1EtcGgRcptde(vo.getGnrlRnk1EtcGgRcptde())
                .gnrlRnk2CrspareaEndde(vo.getGnrlRnk2CrspareaEndde())
                .gnrlRnk2CrspareaRcptde(vo.getGnrlRnk2CrspareaRcptde())
                .gnrlRnk2EtcAreaEndde(vo.getGnrlRnk2EtcAreaEndde())
                .gnrlRnk2EtcAreaRcptde(vo.getGnrlRnk2EtcAreaRcptde())
                .gnrlRnk2EtcGgEndde(vo.getGnrlRnk2EtcGgEndde())
                .gnrlRnk2EtcGgRcptde(vo.getGnrlRnk2EtcGgRcptde())
                .hmpgAdres(vo.getHmpgAdres())
                .houseManageNo(vo.getHouseManageNo())
                .houseNm(vo.getHouseNm())
                .houseSecd(vo.getHouseSecd())
                .houseSecdNm(vo.getHouseSecdNm())
                .hssplyAdres(vo.getHssplyAdres())
                .hssplyZip(vo.getHssplyZip())
                .mDatTrgetAreaSecd(vo.getMDatTrgetAreaSecd())
                .mdhsTelno(vo.getMdhsTelno())
                .mvnPrearngeYm(vo.getMvnPrearngeYm() != null ? vo.getMvnPrearngeYm().toString().substring(0, 7).replace("-", "") : null)
                .nplnPrvorpPublicHouseAt(vo.getNplnPrvorpPublicHouseAt())
                .przwnerPresnatnDe(vo.getPrzwnerPresnatnDe())
                .publicHouseEarthAt(vo.getPublicHouseEarthAt())
                .publicHouseSpclwApplcAt(vo.getPublicHouseSpclwApplcAt())
                .rcritPblancDe(vo.getRcritPblancDe())
                .rentSecdNm(vo.getRentSecdNm())
                .specltRdnEarthAt(vo.getSpecltRdnEarthAt())
                .spsplyRceptBgnde(vo.getSpsplyRceptBgnde())
                .spsplyRceptEgnde(vo.getSpsplyRceptEgnde())
                .subscrptAreaCode(vo.getSubscrptAreaCode())
                .subscrptAreaCodeNm(vo.getSubscrptAreaCodeNm())
                .totSuplyHshldco(vo.getTotSuplyHshldco())
                .build();
    }

    public class DateParser {
        private static final DateTimeFormatter YM_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");

        public static Date parseDate(String yyyymm) {
            if (yyyymm == null || yyyymm.isEmpty()) return null;
            try {
                YearMonth ym = YearMonth.parse(yyyymm.trim(), YM_FORMAT);
                return java.sql.Date.valueOf(ym.atDay(1)); // LocalDate로 1일자 기준 변환
            } catch (Exception e) {
                return null;
            }
        }

        public static Date parseFullDate(String yyyymmdd) {
            if (yyyymmdd == null || yyyymmdd.isEmpty()) return null;
            try {
                LocalDate date = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
                return java.sql.Date.valueOf(date);
            } catch (Exception e) {
                return null;
            }
        }

    }
}
