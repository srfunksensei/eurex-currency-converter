package com.mb.ecb.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Milan Brankovic
 */
@Configuration
@ConfigurationProperties(prefix = "ecb")
@Data
public class EcbProperties {

    @NotBlank
    private String exchangeRateLatestUrl;

    @NotBlank
    private String exchangeRatesHistoryUrl;

    @NotNull
    private List<String> fromCurrencies;

    @NotNull
    private List<String> toCurrencies;
}
