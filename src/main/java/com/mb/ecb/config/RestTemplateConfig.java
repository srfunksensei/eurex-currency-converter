package com.mb.ecb.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Milan Brankovic
 */
@Configuration
public class RestTemplateConfig {

    public static final String ECB_REST_TEMPLATE = "ecbRestTemplate";

    @Bean(name = ECB_REST_TEMPLATE)
    public RestTemplate ecbRestTemplate(final RestTemplateBuilder builder) {
        return createTemplate(builder);
    }

    private RestTemplate createTemplate(final RestTemplateBuilder builder) {
//        final BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return builder
//                .requestFactory(() -> requestFactory)
                .build();
    }
}
