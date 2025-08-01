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

/* =========================================================
   LH 공고·단지 시스템 전체 테이블 생성 스크립트 (MySQL)
   ========================================================= */

-- 1. LH 공고
DROP TABLE IF EXISTS notice;
CREATE TABLE lh_notice (
                           notice_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '공고 ID',
                           pan_id VARCHAR(64) COMMENT '공고번호',
                           upp_ais_tp_cd VARCHAR(16) COMMENT '공고유형명',
                           ais_tp_cd_nm VARCHAR(16) COMMENT '공고세부유형명',
                           pan_nm VARCHAR(64) COMMENT '공고명',
                           cnp_cd_nm VARCHAR(16) COMMENT '지역명',
                           pan_ss VARCHAR(16) COMMENT '공고상태',
                           all_cnt VARCHAR(16) COMMENT '전체조회건수',
                           pan_nt_st_dt DATE COMMENT '공고 발행 날짜',
                           dtl_url VARCHAR(256) COMMENT '공고 상세 URL',
                           spl_inf_tp_cd VARCHAR(8) COMMENT '공급정보구분코드',
                           ccr_cnnt_sys_ds_cd VARCHAR(8),
                           ais_tp_cd VARCHAR(8) COMMENT '공고유형코드'
) COMMENT='LH 공고 테이블';

-- 2. 단지
DROP TABLE IF EXISTS danzi;
CREATE TABLE danzi (
                       danzi_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '단지 ID',
                       bzdt_nm VARCHAR(64) COMMENT '단지명',
                       lct_ara_adr VARCHAR(128) COMMENT '단지 주소',
                       lct_ara_dtl_adr VARCHAR(128) COMMENT '단지상세주소',
                       min_max_rsdn_ddo_ar VARCHAR(128) COMMENT '전용면적',
                       sum_tot_hsh_cnt INT COMMENT '총세대수',
                       htn_fmla_de_cd_nm VARCHAR(16) COMMENT '난방방식',
                       mvin_xpc_ym DATE COMMENT '입주예정일'
) COMMENT='단지 기본 정보 테이블';

-- 3. LH 공고 첨부파일
DROP TABLE IF EXISTS notice_att;
CREATE TABLE lh_notice_att (
                               notice_att_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '공고 첨부파일 ID',
                               notice_id INT COMMENT '공고 ID',
                               sl_pan_ahfl_ds_cd_nm VARCHAR(32) COMMENT '파일구분명',
                               cmn_ahfl_nm VARCHAR(64) COMMENT '첨부파일명',
                               ahfl_url VARCHAR(2048) COMMENT '다운로드 URL',
                               FOREIGN KEY (notice_id) REFERENCES lh_notice(notice_id)
) COMMENT='LH 공고 첨부파일 테이블';

-- 4. 단지 첨부파일
DROP TABLE IF EXISTS danzi_att;
CREATE TABLE danzi_att (
                           danzi_att_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '단지 첨부파일 ID',
                           danzi_id INT COMMENT '단지 ID',
                           fl_ds_cd_nm VARCHAR(64) COMMENT '파일구분명',
                           cmn_ahfl_nm VARCHAR(64) COMMENT '첨부파일명',
                           ahfl_url VARCHAR(2048) COMMENT '첨부파일 URL',
                           FOREIGN KEY (danzi_id) REFERENCES danzi(danzi_id)
) COMMENT='단지 첨부파일 테이블';

-- 5. 공고-단지 매핑
DROP TABLE IF EXISTS notice_danzi;
CREATE TABLE notice_danzi (
                              id INT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',
                              notice_id INT COMMENT '공고 ID',
                              danzi_id INT COMMENT '단지 ID',
                              FOREIGN KEY (notice_id) REFERENCES lh_notice(notice_id),
                              FOREIGN KEY (danzi_id) REFERENCES danzi(danzi_id)
) COMMENT='공고-단지 매핑 테이블';

-- 6. 단지 공급/청약 일정
DROP TABLE IF EXISTS danzi_apply;
CREATE TABLE danzi_apply (
                             apply_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '단지 공급일정 ID',
                             danzi_id INT COMMENT '단지 ID',
                             hs_sbsc_acp_trg_cd_nm VARCHAR(32) COMMENT '구분',
                             sbsc_acp_st_dt DATE COMMENT '접수기간 시작일',
                             sbsc_acp_clsg_dt DATE COMMENT '접수기간 종료일',
                             rmk VARCHAR(32) COMMENT '신청방법(현장접수/인터넷접수)',
                             ppr_sbm_ope_anc_dt DATE COMMENT '서류제출대상자발표일',
                             ppr_acp_st_dt DATE COMMENT '서류접수기간 시작일',
                             prp_acp_clsg_dt DATE COMMENT '서류접수기간 종료일',
                             pzwr_anc_dt DATE COMMENT '당첨자 발표일',
                             pzwr_ppr_sbm_st_dt DATE COMMENT '당첨자 서류제출 시작일',
                             pzwr_ppr_sbm_ed_dt DATE COMMENT '당첨자 서류제출 종료일',
                             ctrt_st_dt DATE COMMENT '계약체결 시작일',
                             ctrt_ed_dt DATE COMMENT '계약체결 종료일',
                             FOREIGN KEY (danzi_id) REFERENCES danzi(danzi_id)
) COMMENT='단지별 청약 및 계약 일정 테이블';
