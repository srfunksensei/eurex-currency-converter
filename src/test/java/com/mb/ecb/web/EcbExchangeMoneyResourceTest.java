package com.mb.ecb.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mb.ecb.config.EcbProperties;
import com.mb.ecb.dto.AvailableCurrenciesDto;
import com.mb.ecb.dto.ExchangeDto;
import com.mb.ecb.dto.ExchangeResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Milan Brankovic
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class EcbExchangeMoneyResourceTest {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper jsonMapper;

    @Autowired
    protected EcbProperties ecbProperties;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void exchange() throws Exception {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(LocalDate.now().minusDays(5))
                .amount(BigDecimal.ONE)
                .build();

        final MvcResult mvcResult = mvc.perform(
                        post("/api/ecb/exchange")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsBytes(dto))
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ExchangeResponseDto result = jsonMapper.readValue(mvcResult.getResponse().getContentAsString(), ExchangeResponseDto.class);
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertNotNull(result.getDate(), "Expected ate");
        Assertions.assertNotNull(result.getAmount(), "Expected amount");
    }

    @Test
    public void exchange_unsupportedCurrency() throws Exception {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("RSD")
                .fromCurrency("EGP")
                .date(LocalDate.now().minusDays(5))
                .amount(BigDecimal.ONE)
                .build();

        mvc.perform(
                        post("/api/ecb/exchange")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsBytes(dto))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void exchange_futureConversion() throws Exception {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(LocalDate.now().plusDays(10))
                .amount(BigDecimal.ONE)
                .build();

        mvc.perform(
                        post("/api/ecb/exchange")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsBytes(dto))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void exchange_notAvailable() throws Exception {
        final ExchangeDto dto = ExchangeDto.builder()
                .toCurrency("USD")
                .fromCurrency("CHF")
                .date(LocalDate.now())
                .amount(BigDecimal.ONE)
                .build();

        mvc.perform(
                        post("/api/ecb/exchange")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsBytes(dto))
                ).andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    public void getAvailableCurrencies() throws Exception {
        final MvcResult mvcResult = mvc.perform(
                        get("/api/ecb/available-currencies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final AvailableCurrenciesDto result = jsonMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), AvailableCurrenciesDto.class);
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertEquals(ecbProperties.getFromCurrencies(), result.getFrom(), "Expected same lists");
        Assertions.assertEquals(ecbProperties.getToCurrencies(), result.getTo(), "Expected same lists");
    }
}
