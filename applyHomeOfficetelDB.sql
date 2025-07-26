use MyHomeCatch;

CREATE TABLE APPLYHOME_officetel
(
    `BSNS_MBY_NM`           VARCHAR(255) COMMENT '사업주체명(시행사)',
    `CNTRCT_CNCLS_BGNDE`    DATE COMMENT '계약시작일',
    `CNTRCT_CNCLS_ENDDE`    DATE COMMENT '계약종료일',
    `HMPG_ADRES`            VARCHAR(500) COMMENT '홈페이지주소(분양건물)',
    `HOUSE_DTL_SECD`        VARCHAR(16) COMMENT '주택상세구분코드(01/02/03/04)',
    `HOUSE_DTL_SECD_NM`     VARCHAR(255) COMMENT '주택상세구분코드명',
    `HOUSE_MANAGE_NO`       VARCHAR(64)             NOT NULL COMMENT '주택관리번호',
    `HOUSE_NM`              VARCHAR(255)            NOT NULL COMMENT '주택명',
    `HOUSE_SECD`            VARCHAR(16) COMMENT '주택구분코드',
    `HOUSE_SECD_NM`         VARCHAR(255) COMMENT '주택구분코드명',
    `HSSPLY_ADRES`          VARCHAR(500) COMMENT '공급위치(주소)',
    `HSSPLY_ZIP`            VARCHAR(16) COMMENT '공급위치우편번호',
    `MDHS_TELNO`            VARCHAR(50) COMMENT '문의처 전화번호(분양문의)',
    `MVN_PREARNGE_YM`       VARCHAR(16) COMMENT '입주예정월',
    `NSPRC_NM`              VARCHAR(255)            NULL DEFAULT NULL COMMENT '신문사',
    `PBLANC_NO`             VARCHAR(64) PRIMARY KEY NOT NULL COMMENT '공고번호',
    `PBLANC_URL`            VARCHAR(500) COMMENT '모집공고상세 URL',
    `PRZWNER_PRESNATN_DE`   DATE COMMENT '당첨자발표일',
    `RCRIT_PBLANC_DE`       DATE COMMENT '모집공고일',
    `SEARCH_HOUSE_SECD`     VARCHAR(16) COMMENT '주택구분(주택구분코드+주택상세구분코드 4글자)',
    `SUBSCRPT_AREA_CODE`    VARCHAR(16) COMMENT '공급지역코드',
    `SUBSCRPT_AREA_CODE_NM` VARCHAR(255) COMMENT '공급지역명',
    `SUBSCRPT_RCEPT_BGNDE`  DATE COMMENT '청약접수시작일',
    `SUBSCRPT_RCEPT_ENDDE`  DATE COMMENT '청약접수종료일',
    `TOT_SUPLY_HSHLDCO`     INT COMMENT '공급규모'
);

SELECT * FROM APPLYHOME_officetel;

CREATE TABLE APPLYHOME_officetel_model
(
    `MODEL_UID`             INT PRIMARY KEY AUTO_INCREMENT,
    `EXCLUSE_AR`            VARCHAR(16) COMMENT '전용면적',
    `GP`                    VARCHAR(16) NULL DEFAULT NULL COMMENT '군',
    `HOUSE_MANAGE_NO`       VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `MODEL_NO`              VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`             VARCHAR(64) NOT NULL COMMENT '공고번호',
    `SUBSCRPT_REQST_AMOUNT` INT COMMENT '청약신청금(만원)',
    `SUPLY_AMOUNT`          INT COMMENT '공급금액(분양최고금액 : 만원)',
    `SUPLY_HSHLDCO`         INT COMMENT '공급세대수',
    `TP`                    VARCHAR(16) COMMENT '타입',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES APPLYHOME_officetel (`PBLANC_NO`)
);


SELECT * FROM APPLYHOME_officetel_model;

SHOW INDEX FROM APPLYHOME_officetel_model;

CREATE TABLE APPLYHOME_officetel_cmpet(
    `CMPET_UID` INT PRIMARY KEY AUTO_INCREMENT,
    `CMPET_RATE` VARCHAR(16) COMMENT '경쟁률',
    `HOUSE_MANAGE_NO`       VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `HOUSE_TY` VARCHAR(32) COMMENT '주택형',
    `MODEL_NO`              VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`             VARCHAR(64) NOT NULL COMMENT '공고번호',
    `REQ_CNT` INT COMMENT '접수건수',
    `RESIDNT_PRIOR_AT` VARCHAR(16) COMMENT '거주자 우선여부(Y/N)',
    `RESIDNT_PRIOR_SENM` VARCHAR(64) COMMENT '공급여부(거주자우선/전체/기타)',
    `SUPLY_HSHLDCO` INT COMMENT '공급세대수',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`, `RESIDNT_PRIOR_SENM`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES APPLYHOME_officetel(`PBLANC_NO`)
);

SELECT * FROM APPLYHOME_officetel_cmpet;

SHOW INDEX FROM APPLYHOME_officetel_cmpet;

select distinct cnp_cd_nm from LH_notice;

select distinct HOUSE_SECD_NM from APPLYHOME_APT;

select * from LH_rental;
select * from LH_housing;

SELECT
    T1.MODEL_UID, T1.EXCLUSE_AR, T1.GP, T1.HOUSE_MANAGE_NO, T1.MODEL_NO, T1.PBLANC_NO,
    T1.SUBSCRPT_REQST_AMOUNT, T1.SUPLY_AMOUNT, T1.SUPLY_HSHLDCO, T1.TP,
    T2.CMPET_UID, T2.CMPET_RATE, T2.HOUSE_TY, T2.REQ_CNT, T2.RESIDNT_PRIOR_AT,
    T2.RESIDNT_PRIOR_SENM, T2.SUPLY_HSHLDCO
FROM
    APPLYHOME_officetel_model T1
        LEFT JOIN
    APPLYHOME_officetel_cmpet T2 ON T1.PBLANC_NO = T2.PBLANC_NO
        AND T1.HOUSE_MANAGE_NO = T2.HOUSE_MANAGE_NO
        AND T1.MODEL_NO = T2.MODEL_NO
WHERE
    T1.PBLANC_NO = 2025950034
ORDER BY T1.MODEL_NO, T2.RESIDNT_PRIOR_AT DESC;

CREATE TABLE APPLYHOME_publicPrivateRent
(
    `BSNS_MBY_NM`           VARCHAR(255) COMMENT '사업주체명(시행사)',
    `CNTRCT_CNCLS_BGNDE`    DATE COMMENT '계약시작일',
    `CNTRCT_CNCLS_ENDDE`    DATE COMMENT '계약종료일',
    `HMPG_ADRES`            VARCHAR(500) COMMENT '홈페이지주소(분양건물)',
    `HOUSE_DETAIL_SECD`     VARCHAR(16) COMMENT '주택상세구분코드',
    `HOUSE_DETAIL_SECD_NM`  VARCHAR(255) COMMENT '주택상세구분코드명',
    `HOUSE_MANAGE_NO`       VARCHAR(64)             NOT NULL COMMENT '주택관리번호',
    `HOUSE_NM`              VARCHAR(255)            NOT NULL COMMENT '주택명',
    `HOUSE_SECD`            VARCHAR(16) COMMENT '주택구분코드',
    `HOUSE_SECD_NM`         VARCHAR(255) COMMENT '주택구분코드명',
    `HSSPLY_ADRES`          VARCHAR(500) COMMENT '공급위치(주소)',
    `HSSPLY_ZIP`            VARCHAR(16) COMMENT '공급위치우편번호',
    `MDHS_TELNO`            VARCHAR(50) COMMENT '문의처 전화번호(분양문의)',
    `MVN_PREARNGE_YM`       VARCHAR(16) COMMENT '입주예정월',
    `NSPRC_NM`              VARCHAR(255)            NULL DEFAULT NULL COMMENT '신문사',
    `PBLANC_NO`             VARCHAR(64) PRIMARY KEY NOT NULL COMMENT '공고번호',
    `PBLANC_URL`            VARCHAR(500) COMMENT '모집공고상세 URL',
    `PRZWNER_PRESNATN_DE`   DATE COMMENT '당첨자발표일',
    `RCRIT_PBLANC_DE`       DATE COMMENT '모집공고일',
    `SEARCH_HOUSE_SECD`     VARCHAR(16) COMMENT '주택구분(주택구분코드+주택상세구분코드 4글자)',
    `SUBSCRPT_AREA_CODE`    VARCHAR(16) COMMENT '공급지역코드',
    `SUBSCRPT_AREA_CODE_NM` VARCHAR(255) COMMENT '공급지역명',
    `SUBSCRPT_RCEPT_BGNDE`  DATE COMMENT '청약접수시작일',
    `SUBSCRPT_RCEPT_ENDDE`  DATE COMMENT '청약접수종료일',
    `TOT_SUPLY_HSHLDCO`     INT COMMENT '공급규모'
);

CREATE TABLE APPLYHOME_publicPrivateRent_model
(
    `MODEL_UID`               INT PRIMARY KEY AUTO_INCREMENT,
    `CNTRCT_AR`               VARCHAR(16) COMMENT '계약면적',
    `EXCLUSE_AR`              VARCHAR(16) COMMENT '전용면적',
    `GNSPLY_HSHLDCO`          INT COMMENT '일반공급세대수',
    `GP`                      VARCHAR(16) NULL DEFAULT NULL COMMENT '군',
    `HOUSE_MANAGE_NO`         VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `MODEL_NO`                VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`               VARCHAR(64) NOT NULL COMMENT '공고번호',
    `SPSPLY_AGED_HSHLDCO`     INT COMMENT '특별공급 고령자 세대수',
    `SPSPLY_NEW_MRRG_HSHLDCO` INT COMMENT '특별공급 신혼 세대수',
    `SPSPLY_YGMN_HSHLDCO`     INT COMMENT '특별공급 청년 세대수',
    `SUBSCRPT_REQST_AMOUNT`   INT COMMENT '청약신청금(만원)',
    `SUPLY_AMOUNT`            INT COMMENT '공급금액(분양최고금액 : 만원)',
    `SUPLY_HSHLDCO`           INT COMMENT '공급세대수',
    `TP`                      VARCHAR(16) COMMENT '타입',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES APPLYHOME_publicPrivateRent (`PBLANC_NO`)
);


CREATE TABLE APPLYHOME_publicPrivateRent_cmpet
(
    `CMPET_UID`          INT PRIMARY KEY AUTO_INCREMENT,
    `CMPET_RATE`         VARCHAR(16) COMMENT '경쟁률',
    `HOUSE_MANAGE_NO`    VARCHAR(64) NOT NULL COMMENT '주택관리번호',
    `HOUSE_TY`           VARCHAR(32) COMMENT '주택형',
    `MODEL_NO`           VARCHAR(32) COMMENT '모델번호',
    `PBLANC_NO`          VARCHAR(64) NOT NULL COMMENT '공고번호',
    `REQ_CNT`            INT COMMENT '접수건수',
    `SPSPLY_KND_CODE`   VARCHAR(16) COMMENT '공급유형코드(00/SN/SO/SY)',
    `SPSPLY_KND_HSHLDCO`   INT COMMENT '배정세대수',
    `SPSPLY_KND_NM`   VARCHAR(16) COMMENT '공급유형(일반공급/신혼부부/고령자/청년)',
    `SUPLY_HSHLDCO`      INT COMMENT '공급세대수',
    UNIQUE (`PBLANC_NO`, `MODEL_NO`, `SPSPLY_KND_NM`),
    FOREIGN KEY (`PBLANC_NO`) REFERENCES APPLYHOME_publicPrivateRent (`PBLANC_NO`)
);