package org.scoula.summary.service;

public interface SummaryService {

    public String getOrCreateSummary(int danziId, String pdfUrl);
    public String getOrCreateMarkdownSummary(int danziId, String pdfUrl);
}
