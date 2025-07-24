package org.scoula.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiConfig {
    @Value("${api.baseUrl}")
    private String baseUrl;

    @Value("${api.apiPath}")
    private String apiPath;

    @Value("${api.serviceKey}")
    private String serviceKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
