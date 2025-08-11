package org.scoula.summary.service;

import org.scoula.summary.mapper.SummaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private SummaryMapper summaryMapper;

    @Override
    public String getOrCreateSummary(String panId, String pdfUrl) {

        String summary = summaryMapper.findByPanId(panId);
        if (summary != null) {
            return summary;
        }

        String text = pdfService.extractTextFromUrl(pdfUrl);

        // 현재는 추출 내용 10글자 저장 -> GPT 연결하면 요약 내용을 저장
        String shortSummary = text.replaceAll("\\s+", "")
                .substring(0, Math.min(10, text.length()));

        summaryMapper.insertSummary(panId, shortSummary);

        return shortSummary;
    }





}
