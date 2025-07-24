package org.scoula.applyHome.dto;

public class ApplyHomeModelDTO {
    // 모델별 달라지는 정보
    private String modelNo; // 모델번호
    private String houseTy; // 주택형 == 모델번호
    private String suplyAr; // 공급면적
    private String resideSecd; // 지역코드(해당/기타)
    // 기타면 포함 지역이 어디까지인지

    private String lttotTopAmount; // 공급금액(분양최고)
    private int reqCnt; // 지원수
    private int suplyHshldco; // 일반 공급세대수
    private int spsplyHshldco; // 특별 총공급세대수

    private int etcHshldco; // 특공-기타세대수
    private int insttRecomendHshldco; // 특-기관추천세대
    private int transrInsttEnfsnHshldco; // 특-이전기관세대
    private int lfeFrstHshldco; // 특- 생애최초
    private int mnychHshldco; // 특-다자녀
    private int nwbbHshldco; // 특-신생아
    private int nwwdsHshldco; // 특-신혼부부
    private int oldParntsSuportHshldco; // 특-노부모
    private int ygmnHshldco; // 특-청년
}
