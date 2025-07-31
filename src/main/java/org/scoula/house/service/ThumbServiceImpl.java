package org.scoula.house.service;


import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.scoula.house.domain.ThumbVO;
import org.scoula.house.mapper.ThumbMapper;
import org.scoula.lh.danzi.domain.DanziAttVO;
import org.scoula.lh.domain.housing.LhHousingAttVO;
import org.scoula.lh.domain.rental.LhRentalAttVO;
import org.scoula.lh.dto.lhHousing.LhHousingAttDTO;
import org.scoula.lh.dto.lhRental.LhRentalAttDTO;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ThumbServiceImpl implements ThumbService {

    private final ThumbMapper mapper;

    private static final String DOMAIN = "https://apply.lh.or.kr"; // 기준이 되는 도메인

    private static void disableSSLCertificateChecking() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String createHousingThumb(LhHousingAttVO vo) {

        LhHousingAttDTO dto = LhHousingAttDTO.of(vo);
        String pageUrl = dto.getAhflUrl();

        disableSSLCertificateChecking();

        try {
            // 1. Jsoup으로 진짜 이미지 src 데이터 가져오기
            Document doc = (Document) Jsoup.connect(pageUrl).get();
            Element img = doc.selectFirst("img");
            if (img == null) {
                System.err.println("이미지 태그를 찾을 수 없습니다: " + pageUrl);
                return null;
            }

            // 2. <img> 태그의 'src' 속성 값
            // 예: /upload/Files/... .jpg
            String relativeUrl = img.attr("src");

            // 3. 상대 경로 앞에 도메인을 붙여 전체 URL 만들기
            String absoluteUrl = DOMAIN + relativeUrl;

            ThumbVO result = ThumbVO.builder().
                    panId(dto.getPanId())
                    .district(dto.getBzdtNm())
                    .flDsCdNm(dto.getSlPanAhflDsCdNm())
                    .imgPath(absoluteUrl)
                    .build();

            mapper.create(result);

            return absoluteUrl;

        } catch (IOException e) {
            System.err.println("페이지 파싱 중 오류 발생: " + pageUrl);
            throw new RuntimeException(e);
        }

    }

    @Override
    public String createRentalThumb(LhRentalAttVO vo) {

        LhRentalAttDTO dto = LhRentalAttDTO.of(vo);
        String pageUrl = dto.getDownloadUrl();

        disableSSLCertificateChecking();

        try {
            // 1. Jsoup으로 진짜 이미지 src 데이터 가져오기
            Document doc = (Document) Jsoup.connect(pageUrl).get();
            Element img = doc.selectFirst("img");
            if (img == null) {
                System.err.println("이미지 태그를 찾을 수 없습니다: " + pageUrl);
                return null;
            }

            // 2. <img> 태그의 'src' 속성 값
            // 예: /upload/Files/... .jpg
            String relativeUrl = img.attr("src");

            // 3. 상대 경로 앞에 도메인을 붙여 전체 URL 만들기
            String absoluteUrl = DOMAIN + relativeUrl;

            ThumbVO result = ThumbVO.builder().
                    panId(dto.getPanId())
                    .district(dto.getHouseName())
                    .flDsCdNm(dto.getFileTypeName())
                    .imgPath(absoluteUrl)
                    .build();

            mapper.create(result);

            return absoluteUrl;

        } catch (IOException e) {
            System.err.println("페이지 파싱 중 오류 발생: " + pageUrl);
            throw new RuntimeException(e);
        }

    }

    @Override
    public DanziAttVO createDanziThumbVO(DanziAttVO vo) {

        String pageUrl = vo.getAhflUrl();

        disableSSLCertificateChecking();

        try {
            // 1. Jsoup으로 진짜 이미지 src 데이터 가져오기
            Document doc = (Document) Jsoup.connect(pageUrl).get();
            Element img = doc.selectFirst("img");
            if (img == null) {
                System.err.println("이미지 태그를 찾을 수 없습니다: " + pageUrl);
                return null;
            }

            // 2. <img> 태그의 'src' 속성 값
            // 예: /upload/Files/... .jpg
            String relativeUrl = img.attr("src");

            // 3. 상대 경로 앞에 도메인을 붙여 전체 URL 만들기
            String absoluteUrl = DOMAIN + relativeUrl;

            // DB 저장용 VO 반환
            DanziAttVO returnVO = DanziAttVO.builder()
                    .danziId(vo.getDanziId())
                    .flDsCdNm(vo.getFlDsCdNm())
                    .cmnAhflNm(vo.getCmnAhflNm())
                    .ahflUrl(absoluteUrl)
                    .build();

            return returnVO;

        } catch (IOException e) {
            System.err.println("페이지 파싱 중 오류 발생: " + pageUrl);
            throw new RuntimeException(e);
        }

    }

}