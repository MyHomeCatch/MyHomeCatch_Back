package org.scoula.summary.service;

import org.scoula.lh.danzi.dto.NoticeSummaryDTO;

public interface ParsedSummaryService {
    public NoticeSummaryDTO createFromMarkdown(int danziId, String markdown);
    public NoticeSummaryDTO getSummary(int danziId);
}
