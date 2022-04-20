package com.mb.ecb.connector;

import com.mb.ecb.config.EcbProperties;
import com.mb.ecb.config.RestTemplateConfig;
import com.mb.ecb.exception.EcbFetchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Milan Brankovic
 */
@Slf4j
@Component
public class EcbConnector {

    private final EcbProperties ecbProperties;
    private final RestTemplate restTemplate;

    public EcbConnector(final EcbProperties ecbProperties,
                        @Qualifier(RestTemplateConfig.ECB_REST_TEMPLATE) final RestTemplate restTemplate) {
        this.ecbProperties = ecbProperties;
        this.restTemplate = restTemplate;
    }

    public String fetchHistoricalExchangeRates() throws EcbFetchException {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(ecbProperties.getExchangeRatesHistoryUrl());
        return fetchFrom(uriBuilder);
    }

    public String fetchLastExchangeRates() throws EcbFetchException {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(ecbProperties.getExchangeRateLatestUrl());
        return fetchFrom(uriBuilder);
    }

    private String fetchFrom(final UriComponentsBuilder uriBuilder) throws EcbFetchException {
        final String url = uriBuilder.toUriString();
        try {
            return restTemplate.getForObject(url, String.class);
//            final Envelope forObject = restTemplate.getForObject(url, Envelope.class);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new EcbFetchException(e.getLocalizedMessage());
        }
    }
}
