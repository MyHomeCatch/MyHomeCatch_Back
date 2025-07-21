use scoula_db;

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
    `PBLANC_NO`             VARCHAR(20) PRIMARY KEY NOT NULL COMMENT '공고번호',
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