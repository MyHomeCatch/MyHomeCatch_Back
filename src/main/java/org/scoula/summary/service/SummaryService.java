package org.scoula.summary.service;

public interface SummaryService {

    public String getOrCreateSummary(String panId, String pdfUrl);
    public String getOrCreateMarkdownSummary(String panId, String pdfUrl);
}
