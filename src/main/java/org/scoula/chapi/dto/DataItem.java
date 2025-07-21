package org.scoula.chapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DataItem{
	@JsonProperty("BSNS_MBY_NM")
	private String bsnsMbyNm;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("CNTRCT_CNCLS_BGNDE")
	private String cntrctCnclsBgnde;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("CNTRCT_CNCLS_ENDDE")
	private String cntrctCnclsEndde;

	@JsonProperty("HMPG_ADRES")
	private String hmpgAdres;

	@JsonProperty("HOUSE_DTL_SECD")
	private String houseDtlSecd;

	@JsonProperty("HOUSE_DTL_SECD_NM")
	private String houseDtlSecdNm;

	@JsonProperty("HOUSE_MANAGE_NO")
	private String houseManageNo;

	@JsonProperty("HOUSE_NM")
	private String houseNm;

	@JsonProperty("HOUSE_SECD")
	private String houseSecd;

	@JsonProperty("HOUSE_SECD_NM")
	private String houseSecdNm;

	@JsonProperty("HSSPLY_ADRES")
	private String hssplyAdres;

	@JsonProperty("HSSPLY_ZIP")
	private String hssplyZip;

	@JsonProperty("MDHS_TELNO")
	private String mdhsTelno;

	@JsonProperty("MVN_PREARNGE_YM")
	private String mvnPrearngeYm;

	@JsonProperty("NSPRC_NM")
	private String nsprcNm;

	@JsonProperty("PBLANC_NO")
	private String pblancNo;

	@JsonProperty("PBLANC_URL")
	private String pblancUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("PRZWNER_PRESNATN_DE")
	private String przwnerPresnatnDe;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("RCRIT_PBLANC_DE")
	private Date rcritPblancDe;

	@JsonProperty("SEARCH_HOUSE_SECD")
	private String searchHouseSecd;

	@JsonProperty("SUBSCRPT_AREA_CODE")
	private String subscrptAreaCode;

	@JsonProperty("SUBSCRPT_AREA_CODE_NM")
	private String subscrptAreaCodeNm;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("SUBSCRPT_RCEPT_BGNDE")
	private Date subscrptReceptBgnde;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@JsonProperty("SUBSCRPT_RCEPT_ENDDE")
	private Date subscrptReceptEndde;

	@JsonProperty("TOT_SUPLY_HSHLDCO")
	private int totSuplyHshldco;
}