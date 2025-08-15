package org.scoula.summary.service;

import org.scoula.lh.danzi.dto.JsonSummaryDTO;
import org.scoula.lh.danzi.dto.NoticeSummaryDTO;

public interface ParsedSummaryService {
    public NoticeSummaryDTO createFromMarkdown(int danziId, String markdown);
    public JsonSummaryDTO getJsonSummary(int danziId);
    public JsonSummaryDTO createFromJson(int danziId, String json);
}
