package org.scoula.summary.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class PdfServiceImpl implements PdfService {

   @Override
   public String extractTextFromUrl(String pdfUrl) {
       try {
           // SSL 인증 무시 세팅
           trustAllCertificates();

           try (InputStream input = new URL(pdfUrl).openStream();
                PDDocument document = PDDocument.load(input)) {

               PDFTextStripper stripper = new PDFTextStripper();
               stripper.setSortByPosition(true);
               return stripper.getText(document);
           }

       } catch (Exception e) {
           throw new RuntimeException("PDF 텍스트 추출 실패: " + e.getMessage(), e);
       }
   }

    private void trustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
