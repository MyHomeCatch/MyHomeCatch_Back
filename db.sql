use MyHomeCatch;
drop table  APPLYHOME_APT;
CREATE TABLE APPLYHOME_APT (
                               PBLANC_NO                      VARCHAR(20)    PRIMARY KEY,
                               BSNS_MBY_NM                    VARCHAR(100)   NULL,
                               CNSTRCT_ENTRPS_NM              VARCHAR(100)   NULL,
                               CNTRCT_CNCLS_BGNDE             DATE           NOT NULL,
                               CNTRCT_CNCLS_ENDDE             DATE           NOT NULL,
                               GNRL_RNK1_CRSPAREA_ENDDE       DATE           NULL,
                               GNRL_RNK1_CRSPAREA_RCPTDE      DATE           NULL,
                               GNRL_RNK1_ETC_AREA_ENDDE       DATE           NULL,
                               GNRL_RNK1_ETC_AREA_RCPTDE      DATE           NULL,
                               GNRL_RNK1_ETC_GG_ENDDE         DATE           NULL,
                               GNRL_RNK1_ETC_GG_RCPTDE        DATE           NULL,
                               GNRL_RNK2_CRSPAREA_ENDDE       DATE           NULL,
                               GNRL_RNK2_CRSPAREA_RCPTDE      DATE           NULL,
                               GNRL_RNK2_ETC_AREA_ENDDE       DATE           NULL,
                               GNRL_RNK2_ETC_AREA_RCPTDE      DATE           NULL,
                               GNRL_RNK2_ETC_GG_ENDDE         DATE           NULL,
                               GNRL_RNK2_ETC_GG_RCPTDE        DATE           NULL,
                               HMPG_ADRES                     VARCHAR(200)   NULL,
                               HOUSE_DTL_SECD                 CHAR(2)        NOT NULL,
                               HOUSE_DTL_SECD_NM              VARCHAR(50)    NOT NULL,
                               HOUSE_MANAGE_NO                VARCHAR(20)    NOT NULL,
                               HOUSE_NM                       VARCHAR(100)   NOT NULL,
                               HOUSE_SECD                     CHAR(2)        NOT NULL,
                               HOUSE_SECD_NM                  VARCHAR(50)    NOT NULL,
                               HSSPLY_ADRES                   VARCHAR(200)   NULL,
                               HSSPLY_ZIP                     VARCHAR(10)    NULL,
                               IMPRMN_BSNS_AT                 CHAR(1)        NOT NULL,
                               LRSCL_BLDLND_AT                CHAR(1)        NOT NULL,
                               MDAT_TRGET_AREA_SECD           CHAR(1)        NOT NULL,
                               MDHS_TELNO                     VARCHAR(20)    NULL,
                               MVN_PREARNGE_YM                CHAR(6)        NULL,
                               NPLN_PRVOPR_PUBLIC_HOUSE_AT    CHAR(1)        NOT NULL,
                               NSPR_C_NM                      VARCHAR(50)    NULL,
                               PBLANC_URL                     VARCHAR(300)   NULL,
                               PRZWNER_PRESNATN_DE            DATE           NULL,
                               PUBLIC_HOUSE_EARTH_AT          CHAR(1)        NOT NULL,
                               PUBLIC_HOUSE_SPCLW_APPLC_AT    CHAR(1)        NOT NULL,
                               RCEPT_BGNDE                    DATE           NOT NULL,
                               RCEPT_ENDDE                    DATE           NOT NULL,
                               RCRIT_PBLANC_DE                DATE           NOT NULL,
                               RENT_SECD                      CHAR(1)        NOT NULL,
                               RENT_SECD_NM                   VARCHAR(50)    NOT NULL,
                               SPECLT_RDN_EARTH_AT            CHAR(1)        NOT NULL,
                               SPSPLY_RCEPT_BGNDE             DATE           NULL,
                               SPSPLY_RCEPT_ENDDE             DATE           NULL,
                               SUBSCRPT_AREA_CODE             VARCHAR(10)    NOT NULL,
                               SUBSCRPT_AREA_CODE_NM          VARCHAR(50)    NOT NULL,
                               TOT_SUPLY_HSHLDCO              INT            NOT NULL
);

drop table APPLYHOME_APT_competition;
TRUNCATE TABLE APPLYHOME_APT_competition;
CREATE TABLE APPLYHOME_APT_competition (
                                           cmpet_ID              INT AUTO_INCREMENT PRIMARY KEY,       -- PK
                                           PBLANC_NO             VARCHAR(64),                          -- FK: 공고번호
                                           CMPET_RATE            VARCHAR(16),                          -- 경쟁률
                                           HOUSE_MANAGE_NO       VARCHAR(64),                          -- 주택관리번호
                                           HOUSE_TY              VARCHAR(32),                          -- 주택형
                                           MODEL_NO              VARCHAR(32),                          -- 모델번호
                                           REQ_CNT               INT,                                  -- 접수건수
                                           RESIDE_SECD           VARCHAR(16),                          -- 거주코드(01/02)
                                           RESIDE_SENM           VARCHAR(32),                          -- 거주지역
                                           SUBSCRPT_RANK_CODE    INT,                                  -- 순위(1/2)
                                           SUPLY_HSHLDCO         INT,                                  -- 공급세대수
                                           LWET_SCORE            VARCHAR(8),                                  -- 최저점
                                           TOP_SCORE             VARCHAR(8),                                  -- 최고점
                                           AVRG_SCORE            VARCHAR(8),                                  -- 평균점
                                           Constraint apt_cmpet unique (PBLANC_NO, MODEL_NO,RESIDE_SECD)
);

CREATE TABLE APPLYHOME_APT_special (
                                       special_id                      INT             AUTO_INCREMENT PRIMARY KEY,
                                       PBLANC_NO                       VARCHAR(64)     NOT NULL,    -- 공고번호 (FK to APPLYHOME_APT)
                                       HOUSE_MANAGE_NO                 VARCHAR(64)     NOT NULL,    -- 주택관리번호
                                       HOUSE_TY                        VARCHAR(32)     NOT NULL,    -- 주택형
                                       MODEL_NO                        VARCHAR(32)     NOT NULL,    -- 모델번호
                                       ETC_HSHLDCO                     INT             NOT NULL,    -- 기타공급세대수
                                       INSTT_RECOMEND_HSHLDCO          INT             NOT NULL,    -- 기관추천공급세대수
                                       LFE_FRST_HSHLDCO                INT             NOT NULL,    -- 생애최초공급세대수
                                       LTTOT_TOP_AMOUNT                VARCHAR(16)     NOT NULL,    -- 분양금액(최고)
                                       MNYCH_HSHLDCO                   INT             NOT NULL,    -- 다자녀공급세대수
                                       NWBB_HSHLDCO                    INT             NOT NULL,    -- 신혼부부공급세대수
                                       NWWDS_HSHLDCO                   INT             NOT NULL,    -- 노부모부양공급세대수
                                       OLD_PARNTS_SUPORT_HSHLDCO       INT             NOT NULL,    -- 노부모부양공급세대수(구)
                                       SPSPLY_HSHLDCO                  INT             NOT NULL,    -- 특별공급총세대수
                                       SUPLY_AR                        VARCHAR(16)     NOT NULL,    -- 공급면적
                                       SUPLY_HSHLDCO                   INT             NOT NULL,    -- 공급세대수
                                       TRANSR_INSTT_ENFSN_HSHLDCO      INT             NOT NULL,    -- 이관기관공급세대수
                                       YGMN_HSHLDCO                    INT             NOT NULL,    -- 영구임대공급세대수

                                       CONSTRAINT uq_APPLYHOME_APT_special_unique
                                           UNIQUE (PBLANC_NO, MODEL_NO)
);

-- 삭제할 행들을 확인하는 SELECT 문 (안전하게 먼저 실행)
SELECT t1.*
FROM lh_thumb t1
         INNER JOIN lh_thumb t2
WHERE
    t1.att_id > t2.att_id AND  -- id가 더 큰 쪽을 중복으로 간주
    t1.pan_id = t2.pan_id AND t1.district = t2.district AND t1.fl_ds_cd_nm = t2.fl_ds_cd_nm;
