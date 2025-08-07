package org.scoula.house.util;

import org.scoula.lh.domain.LhNoticeVO;

public class SupplyTypeMapper {

    /**
     * LhNoticeVO 객체로부터 주택 유형 문자열 반환
     *
     * @param lhNoticeVO LhNoticeVO 객체
     * @return 주택 유형 문자열
     */
    public static String getHousingTypeString(LhNoticeVO lhNoticeVO) {
        if (lhNoticeVO == null) {
            return "알 수 없는 유형";
        }

        return getHousingTypeString(lhNoticeVO.getSplInfTpCd());
    }

    /**
     * SPL_INF_TP_CD와 CCR_CNNT_SYS_DS_CD에 따른 주택 유형 문자열 반환
     *
     * @param splInfTpCd     SPL_INF_TP_CD 값
     * @return 주택 유형 문자열
     */
    private static String getHousingTypeString(String splInfTpCd) {
        if (splInfTpCd == null) {
            return "알 수 없는 유형";
        }

        return switch (splInfTpCd) {
            case "050" -> "분양주택";
            case "051" -> "분양주택-분양전환";
            case "060" -> "공공임대(5년, 10년, 분납임대)";
            case "061" -> "임대주택-50년공공임대";
            case "062" -> "국민임대 / 장기전세 / 신축다세대 / 영구임대";
            case "063" -> "행복주택";
            case "064" -> "가정어린이집";
            case "390" -> "신혼희망타운";
            default -> "알 수 없는 유형";
        };
    }
}