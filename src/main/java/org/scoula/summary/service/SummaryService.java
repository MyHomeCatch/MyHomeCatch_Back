package org.scoula.summary.service;

import org.scoula.lh.danzi.dto.NoticeSummaryDTO;

public interface SummaryService {

    public String getOrCreateSummary(int danziId, String pdfUrl);
    public String getOrCreateMarkdownSummary(int danziId, String pdfUrl);
    public void fillAllSummaries();
    public String getNoticeSummary(int danziId);
    public String getOrCreateJsonSummary(int danziId, String pdfUrl);
}
