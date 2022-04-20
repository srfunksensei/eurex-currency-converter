package com.mb.ecb.connector;

import com.mb.ecb.config.EcbProperties;
import com.mb.ecb.exception.EcbFetchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Milan Brankovic
 */
public class EcbConnectorTest {

    private EcbConnector underTest;
    private EcbProperties ecbProperties;
    private RestTemplate ecbRestTemplate;

    @BeforeEach
    public void setUp() {
        ecbRestTemplate = Mockito.mock(RestTemplate.class);

        ecbProperties = new EcbProperties();
        ecbProperties.setExchangeRatesHistoryUrl("http://localhost:8080");
        ecbProperties.setExchangeRateLatestUrl("http://localhost:8080");

        underTest = new EcbConnector(ecbProperties, ecbRestTemplate);
    }

    @Test
    public void fetchHistoricalExchangeRates_Success() throws EcbFetchException {
        final String toReturn = "result";
        when(ecbRestTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(toReturn);

        final String result = underTest.fetchHistoricalExchangeRates();

        Mockito.verify(ecbRestTemplate, times(1))
                .getForObject(ecbProperties.getExchangeRatesHistoryUrl(), String.class);

        Assertions.assertEquals(toReturn, result, "Expected different result");
    }

    @Test
    public void fetchHistoricalExchangeRates_Error() {
        when(ecbRestTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(
                EcbFetchException.class,
                () -> underTest.fetchHistoricalExchangeRates(),
                "Expected fetchHistoricalExchangeRate() to throw, but it didn't"
        );

        Mockito.verify(ecbRestTemplate, times(1))
                .getForObject(ecbProperties.getExchangeRatesHistoryUrl(), String.class);
    }

    @Test
    public void fetchLastExchangeRates_Success() throws EcbFetchException {
        final String toReturn = "result";
        when(ecbRestTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(toReturn);

        final String result = underTest.fetchLastExchangeRates();

        Mockito.verify(ecbRestTemplate, times(1))
                .getForObject(ecbProperties.getExchangeRateLatestUrl(), String.class);

        Assertions.assertEquals(toReturn, result, "Expected different result");
    }

    @Test
    public void fetchLastExchangeRates_Error() {
        when(ecbRestTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(
                EcbFetchException.class,
                () -> underTest.fetchLastExchangeRates(),
                "Expected fetchHistoricalExchangeRate() to throw, but it didn't"
        );

        Mockito.verify(ecbRestTemplate, times(1))
                .getForObject(ecbProperties.getExchangeRateLatestUrl(), String.class);
    }
}
