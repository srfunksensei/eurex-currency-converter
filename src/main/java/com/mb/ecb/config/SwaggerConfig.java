package com.mb.ecb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @author Milan Brankovic
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiPublic() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mb.ecb.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfoPublic());
    }

    private ApiInfo apiInfoPublic() {
        return new ApiInfo(
                "ECB exchange money REST API",
                "API for exchanging money",
                "V1",
                "Terms of service",
                new Contact("ECB", "localhost:8080", "issues@ecb.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
