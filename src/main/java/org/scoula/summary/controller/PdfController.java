package org.scoula.summary.controller;

import org.scoula.summary.service.PdfService;
import org.scoula.summary.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    PdfService pdfService;

    @GetMapping("/extract")
    public void extractText(@RequestParam("url") String pdfUrl, HttpServletResponse response) throws IOException {
        String text = pdfService.extractTextFromUrl(pdfUrl);
        String cleanText = TextUtils.cleanPdfText(text);
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write(cleanText);
    }
}
