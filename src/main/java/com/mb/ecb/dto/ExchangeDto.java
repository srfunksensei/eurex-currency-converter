package com.mb.ecb.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mb.ecb.parser.XmlEcbParser;
import com.mb.ecb.serializer.MoneySerializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Milan Brankovic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeDto {

    @NotBlank
    private String fromCurrency;
    @NotBlank
    private String toCurrency;
    @NotNull
    @JsonSerialize(using = MoneySerializer.class)
    @Min(0)
    private BigDecimal amount;
    @NotNull
    @Pattern(regexp = XmlEcbParser.DATE_PATTERN)
    private LocalDate date;
}
