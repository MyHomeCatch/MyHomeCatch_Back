use scoula_db;

# DROP TABLE IF EXISTS  ch_db_officetel;

CREATE TABLE ch_db_officetel
(
    `BSNS_MBY_NM`           VARCHAR(255) COMMENT '사업주체명',
    `CNTRCT_CNCLS_BGNDE`    DATE COMMENT '계약체결시작일',
    `CNTRCT_CNCLS_ENDDE`    DATE COMMENT '계약체결종료일',
    `HMPG_ADRES`            VARCHAR(500) COMMENT '홈페이지주소',
    `HOUSE_DTL_SECD`        VARCHAR(16) COMMENT '주택상세구분코드',
    `HOUSE_DTL_SECD_NM`     VARCHAR(255) COMMENT '주택상세구분코드명',
    `HOUSE_MANAGE_NO`       VARCHAR(64)             NOT NULL COMMENT '주택관리번호',
    `HOUSE_NM`              VARCHAR(255)            NOT NULL COMMENT '주택명',
    `HOUSE_SECD`            VARCHAR(16) COMMENT '주택구분코드',
    `HOUSE_SECD_NM`         VARCHAR(255) COMMENT '주택구분코드명',
    `HSSPLY_ADRES`          VARCHAR(500) COMMENT '공급위치',
    `HSSPLY_ZIP`            VARCHAR(16) COMMENT '공급위치우편번호',
    `MDHS_TELNO`            VARCHAR(50) COMMENT '문의처연락처',
    `MVN_PREARNGE_YM`       VARCHAR(16) COMMENT '입주예정월',
    `NSPRC_NM`              VARCHAR(255)            NULL DEFAULT NULL COMMENT '뉴스명',
    `PBLANC_NO`             VARCHAR(64) PRIMARY KEY NOT NULL COMMENT '공고번호',
    `PBLANC_URL`            VARCHAR(500) COMMENT '모집공고URL',
    `PRZWNER_PRESNATN_DE`   DATE COMMENT '당첨자발표일',
    `RCRIT_PBLANC_DE`       DATE COMMENT '모집공고일',
    `SEARCH_HOUSE_SECD`     VARCHAR(16) COMMENT '검색주택구분코드',
    `SUBSCRPT_AREA_CODE`    VARCHAR(16) COMMENT '공급지역코드',
    `SUBSCRPT_AREA_CODE_NM` VARCHAR(255) COMMENT '공급지역명',
    `SUBSCRPT_RCEPT_BGNDE`  DATE COMMENT '청약접수시작일',
    `SUBSCRPT_RCEPT_ENDDE`  DATE COMMENT '청약접수종료일',
    `TOT_SUPLY_HSHLDCO`     INT COMMENT '총공급세대수'
);

SELECT * FROM ch_db_officetel;

# DROP TABLE IF EXISTS ch_db_officetel_model;

CREATE TABLE ch_db_officetel_model
(
    `ID`                    INT PRIMARY KEY AUTO_INCREMENT,
    `EXCLUSE_AR`            VARCHAR(16) COMMENT '전용면적',
    `GP`                    VARCHAR(16) COMMENT '군',
    `HOUSE_MANAGE_NO`       VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `MODEL_NO`              VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`             VARCHAR(64) NOT NULL COMMENT '공고번호',
    `SUBSCRPT_REQST_AMOUNT` VARCHAR(32) COMMENT '청약신청금(만원)',
    `SUPLY_AMOUNT`          VARCHAR(32) COMMENT '공급금액(분양최고금액 : 만원)',
    `SUPLY_HSHLDCO`         INT COMMENT '공급세대수',
    `TP`                    VARCHAR(16) COMMENT '타입',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES ch_db_officetel (`PBLANC_NO`)
);

SELECT * FROM ch_db_officetel_model;

SHOW INDEX FROM ch_db_officetel_model;

# DROP TABLE IF EXISTS ch_db_officetel_cmpet;

CREATE TABLE ch_db_officetel_cmpet(
    `CMPET_ID` INT PRIMARY KEY AUTO_INCREMENT,
    `CMPET_RATE` VARCHAR(16) COMMENT '경쟁률',
    `HOUSE_MANAGE_NO`       VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `HOUSE_TY` VARCHAR(32) COMMENT '주택형',
    `MODEL_NO`              VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`             VARCHAR(64) NOT NULL COMMENT '공고번호',
    `REQ_CNT` INT COMMENT '접수건수',
    `RESIDNT_PRIOR_AT` VARCHAR(16) COMMENT '거주자 우선여부(Y/N)',
    `RESIDNT_PRIOR_SEMN` VARCHAR(64) COMMENT '공급여부(거주자우선/전체/기타)',
    `SUPLY_HSHLDCO` INT COMMENT '공급세대수',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`, `RESIDNT_PRIOR_SEMN`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES ch_db_officetel(`PBLANC_NO`)
);

SELECT * FROM ch_db_officetel_cmpet;

SHOW INDEX FROM ch_db_officetel_cmpet;