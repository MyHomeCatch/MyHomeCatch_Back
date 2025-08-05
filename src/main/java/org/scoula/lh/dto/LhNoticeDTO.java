package org.scoula.lh.dto;

import java.util.Date;

public class LhNoticeDTO {
    private String panId;
    private String panName;
    private String panState;
    private Date noticeDate;
    private String noticeUrl;
    private String allCnt;
    private String uppAisTpCd;
    private String aisTpCd;
    private String splInfTpCd;
    private String ccrCnntSysDsCd;

    // Getters and Setters
    public String getPanId() {
        return panId;
    }

    public void setPanId(String panId) {
        this.panId = panId;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanState() {
        return panState;
    }

    public void setPanState(String panState) {
        this.panState = panState;
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getAllCnt() {
        return allCnt;
    }

    public void setAllCnt(String allCnt) {
        this.allCnt = allCnt;
    }

    public String getUppAisTpCd() {
        return uppAisTpCd;
    }

    public void setUppAisTpCd(String uppAisTpCd) {
        this.uppAisTpCd = uppAisTpCd;
    }

    public String getAisTpCd() {
        return aisTpCd;
    }

    public void setAisTpCd(String aisTpCd) {
        this.aisTpCd = aisTpCd;
    }

    public String getSplInfTpCd() {
        return splInfTpCd;
    }

    public void setSplInfTpCd(String splInfTpCd) {
        this.splInfTpCd = splInfTpCd;
    }

    public String getCcrCnntSysDsCd() {
        return ccrCnntSysDsCd;
    }

    public void setCcrCnntSysDsCd(String ccrCnntSysDsCd) {
        this.ccrCnntSysDsCd = ccrCnntSysDsCd;
    }
}
