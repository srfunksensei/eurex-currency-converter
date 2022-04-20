package com.mb.ecb.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mb.ecb.parser.XmlEcbParser;
import com.mb.ecb.serializer.MoneySerializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Milan Brankovic
 */
@Data
@Builder
public class ExchangeResponseDto {

    @NotNull
    @JsonSerialize(using = MoneySerializer.class)
    @Min(0)
    private BigDecimal amount;
    @NotNull
    @Pattern(regexp = XmlEcbParser.DATE_PATTERN)
    private LocalDate date;
}
