package org.scoula.summary.controller;

import org.scoula.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public void getSummary(@RequestParam("panId") String panId,
                           @RequestParam("pdfUrl") String pdfUrl,
                           HttpServletResponse response) {

        try {
            String summary = summaryService.getOrCreateSummary(panId, pdfUrl);

            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(summary);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("요약 실패: " + e.getMessage());
            } catch (IOException ioException) {
            }
        }
    }
}
