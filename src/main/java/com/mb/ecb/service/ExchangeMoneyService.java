package com.mb.ecb.service;

import com.mb.ecb.config.EcbProperties;
import com.mb.ecb.connector.EcbConnector;
import com.mb.ecb.dto.ExchangeDto;
import com.mb.ecb.dto.ExchangeResponseDto;
import com.mb.ecb.dto.xml.CurrencyCube;
import com.mb.ecb.exception.FutureConversionNotPossibleException;
import com.mb.ecb.exception.ExchangeRateUnavailableException;
import com.mb.ecb.exception.UnsupportedCurrencyException;
import com.mb.ecb.parser.XmlEcbParser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

/**
 * @author Milan Brankovic
 */
@Service
@Slf4j
@AllArgsConstructor
public class ExchangeMoneyService {

    private final EcbProperties ecbProperties;
    private final EcbConnector ecbConnector;
    private final XmlEcbParser xmlEcbParser;

    protected final ConcurrentHashMap<LocalDate, List<CurrencyCube>> historicExchangeRates = new ConcurrentHashMap<>();

    public List<String> getFromCurrencies() {
        return ecbProperties.getFromCurrencies();
    }

    public List<String> getToCurrencies() {
        return ecbProperties.getToCurrencies();
    }

    public ExchangeResponseDto exchange(@Valid final ExchangeDto exchangeDto) {
        if (!ecbProperties.getFromCurrencies().contains(exchangeDto.getFromCurrency()) ||
                !ecbProperties.getToCurrencies().contains(exchangeDto.getToCurrency())) {
            throw new UnsupportedCurrencyException("Currency for conversion not supported yet!");
        }

        final LocalDate date = exchangeDto.getDate();
        if (date.isAfter(LocalDate.now())) {
            throw new FutureConversionNotPossibleException("Not possible to execute conversion for dates in the future!");
        }

        final LocalDate workingDate = getClosestPastWorkingDay(date);
        if (!historicExchangeRates.containsKey(workingDate)) {
            throw new ExchangeRateUnavailableException("Exchange rate data not available for calculation!");
        }

        final BigDecimal exchangedAmount = exchangeDto.getFromCurrency().equals(exchangeDto.getToCurrency()) ?
                exchangeDto.getAmount() :
                calculate(exchangeDto, workingDate);
        return ExchangeResponseDto.builder()
                .amount(exchangedAmount)
                .date(workingDate)
                .build();
    }

    public void fetchHistoricalExchangeRates() {
        try {
            final String xmlString = ecbConnector.fetchHistoricalExchangeRates();
            historicExchangeRates.putAll(xmlEcbParser.parse(xmlString));
        } catch (Exception e) {
            log.error("Could not populate historic data", e);
        }
    }

    public void fetchLastHistoricExchangeRate() {
        try {
            final String xmlString = ecbConnector.fetchLastExchangeRates();
            historicExchangeRates.putAll(xmlEcbParser.parse(xmlString));
        } catch (Exception e) {
            log.error("Could not add latest data", e);
        }
    }

    protected boolean isWeekend(final LocalDate date) {
        final DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        return SATURDAY == day || SUNDAY == day;
    }

    protected LocalDate getClosestPastWorkingDay(final LocalDate date) {
        if (isWeekend(date)) {
            final LocalDate previousDay = date.minusDays(1),
                    twoDaysBefore = date.minusDays(2);

            return !isWeekend(previousDay) ? previousDay : twoDaysBefore;
        }

        return date;
    }

    private BigDecimal calculate(final ExchangeDto dto, final LocalDate onDate) {
        final List<CurrencyCube> currencyCubes = historicExchangeRates.get(onDate);
        final CurrencyCube fromCurrencyCube = filterFrom(currencyCubes, dto.getFromCurrency()),
                toCurrencyCube = filterFrom(currencyCubes, dto.getToCurrency());

        return dto.getAmount().multiply(toCurrencyCube.getRate())
                .divide(fromCurrencyCube.getRate(), fromCurrencyCube.getRate().scale(), RoundingMode.HALF_UP);
    }

    private CurrencyCube filterFrom(final List<CurrencyCube> currencyCubes, final String currency) {
        return currencyCubes.stream()
                .filter(cube -> cube.getCurrency().equals(currency))
                .findFirst()
                .orElseThrow();
    }
}
