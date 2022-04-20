package com.mb.ecb.service;

import com.mb.ecb.config.EcbProperties;
import com.mb.ecb.connector.EcbConnector;
import com.mb.ecb.dto.ExchangeDto;
import com.mb.ecb.dto.ExchangeResponseDto;
import com.mb.ecb.dto.xml.CurrencyCube;
import com.mb.ecb.exception.*;
import com.mb.ecb.parser.XmlEcbParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Milan Brankovic
 */
public class ExchangeMoneyServiceTest {

    private EcbConnector ecbConnector;
    private XmlEcbParser xmlEcbParser;

    private ExchangeMoneyService underTest;

    private static final List<String> CURRENCIES_LIST = Stream.of("USD", "CHF").collect(Collectors.toList());

    @BeforeEach
    public void setUp() {
        final EcbProperties ecbProperties = new EcbProperties();
        ecbProperties.setExchangeRatesHistoryUrl("http://localhost:8080");
        ecbProperties.setExchangeRateLatestUrl("http://localhost:8080");
        ecbProperties.setFromCurrencies(CURRENCIES_LIST);
        ecbProperties.setToCurrencies(CURRENCIES_LIST);

        ecbConnector = Mockito.mock(EcbConnector.class);
        xmlEcbParser = Mockito.mock(XmlEcbParser.class);

        underTest = new ExchangeMoneyService(ecbProperties, ecbConnector, xmlEcbParser);
    }

    @Test
    public void getFromCurrencies() {
        final List<String> result = underTest.getFromCurrencies();
        Assertions.assertEquals(CURRENCIES_LIST, result, "Expected different result");
    }

    @Test
    public void getToCurrencies() {
        final List<String> result = underTest.getToCurrencies();
        Assertions.assertEquals(CURRENCIES_LIST, result, "Expected different result");
    }

    @Test
    public void fetchHistoricalExchangeRates() throws EcbFetchException, MalformedXmlException {
        when(ecbConnector.fetchHistoricalExchangeRates()).thenReturn("");

        final CurrencyCube currencyCube = CurrencyCube.builder()
                .currency(CURRENCIES_LIST.get(0))
                .rate(BigDecimal.ONE)
                .build();
        final Map<LocalDate, List<CurrencyCube>> toReturn = Map.of(LocalDate.now(), Collections.singletonList(currencyCube));
        when(xmlEcbParser.parse(any(String.class))).thenReturn(toReturn);

        underTest.fetchHistoricalExchangeRates();

        Assertions.assertFalse(underTest.historicExchangeRates.isEmpty(), "Expected different result");
    }

    @Test
    public void fetchLastHistoricExchangeRate() throws EcbFetchException, MalformedXmlException {
        when(ecbConnector.fetchLastExchangeRates()).thenReturn("");

        final Map<LocalDate, List<CurrencyCube>> toReturn = new HashMap<>();
        when(xmlEcbParser.parse(any(String.class))).thenReturn(toReturn);

        underTest.fetchLastHistoricExchangeRate();

        Assertions.assertTrue(underTest.historicExchangeRates.isEmpty(), "Expected different result");
    }

    @Test
    public void exchange_unsupportedCurrency() {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("RSD")
                .fromCurrency("EGP")
                .date(LocalDate.now())
                .amount(BigDecimal.ONE)
                .build();

        Assertions.assertThrows(
                UnsupportedCurrencyException.class,
                () -> underTest.exchange(dto),
                "Expected exchange() to throw, but it didn't"
        );
    }

    @Test
    public void exchange_futureConversion() {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(LocalDate.now().plusDays(10))
                .amount(BigDecimal.ONE)
                .build();

        Assertions.assertThrows(
                FutureConversionNotPossibleException.class,
                () -> underTest.exchange(dto),
                "Expected exchange() to throw, but it didn't"
        );
    }

    @Test
    public void exchange_notAvailable() {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(LocalDate.now())
                .amount(BigDecimal.ONE)
                .build();

        Assertions.assertThrows(
                ExchangeRateUnavailableException.class,
                () -> underTest.exchange(dto),
                "Expected exchange() to throw, but it didn't"
        );
    }

    @Test
    public void exchange_weekend() {
        final LocalDate date = LocalDate.of(2022, 2, 5),
                closestPastWorkingDay = underTest.getClosestPastWorkingDay(date);

        final List<CurrencyCube> currencyCubes = Stream.of(CurrencyCube.builder().currency("USD").rate(XmlEcbParser.parseExchangeRate("1.1435")).build(),
                        CurrencyCube.builder().currency("CHF").rate(XmlEcbParser.parseExchangeRate("1.0555")).build())
                .collect(Collectors.toList());
        underTest.historicExchangeRates.put(closestPastWorkingDay, currencyCubes);

        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(date)
                .amount(BigDecimal.ONE)
                .build();

        final ExchangeResponseDto result = underTest.exchange(dto);
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertEquals(closestPastWorkingDay, result.getDate(), "Expected different result");
        Assertions.assertNotNull(result.getAmount(), "Expected amount");
    }

    @Test
    public void exchange_workingDay() {
        final LocalDate date = LocalDate.of(2022, 2, 10);

        final List<CurrencyCube> currencyCubes = Stream.of(CurrencyCube.builder().currency("USD").rate(XmlEcbParser.parseExchangeRate("1.1435")).build(),
                        CurrencyCube.builder().currency("CHF").rate(XmlEcbParser.parseExchangeRate("1.0555")).build())
                .collect(Collectors.toList());
        underTest.historicExchangeRates.put(date, currencyCubes);

        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(date)
                .amount(BigDecimal.ONE)
                .build();

        final ExchangeResponseDto result = underTest.exchange(dto);
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertEquals(date, result.getDate(), "Expected different result");
        Assertions.assertNotNull(result.getAmount(), "Expected amount");
    }
}
