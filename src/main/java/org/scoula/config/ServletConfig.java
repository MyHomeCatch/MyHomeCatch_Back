package org.scoula.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages = {
    "org.scoula",                    // 전체 기본 패키지
    "org.scoula.exception",          // 예외 처리 관련 패키지
    "org.scoula.controller",         // 웹 컨트롤러
    "org.scoula.statics.controller", // 정적 페이지용 컨트롤러
    "org.scoula.house"               // LH 공고 관련 컨트롤러/서비스 등
})
@PropertySource("classpath:application.properties")
@EnableScheduling
public class ServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**") // url이 /resources/로 시작하는 모든 경로
                .addResourceLocations("/resources/"); // webapp/resources/경로로 매핑



        // Swagger UI 리소스를 위한 핸들러 설정
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        // Swagger WebJar 리소스 설정
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // Swagger 리소스 설정
        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/v2/api-docs")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

        // jsp view resolver 설정 - 뷰 이름을 JSP나 템플릿으로 매핑
        @Override
        public void configureViewResolvers (ViewResolverRegistry registry){
            InternalResourceViewResolver bean = new InternalResourceViewResolver();
            bean.setViewClass(JstlView.class);
            bean.setPrefix("/WEB-INF/views/");
            bean.setSuffix(".jsp");
            registry.viewResolver(bean);
        }

        // Servlet ${WEB_XML_VERSION} 파일 업로드 사용시
        // Dispatcher이 핸들러(컨트롤러) 호출 전에
        // 요청을 파싱해서 MultipartFile을 다룰 수 있도록 MultipartHttpServletRequest로 변환
        // -> file 파라미터 바인딩 file.getOriginalFilename() 등 지원
        @Bean
        public MultipartResolver multipartResolver () {
            StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
            return resolver;
        }
    }

