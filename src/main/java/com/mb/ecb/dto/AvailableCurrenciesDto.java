package com.mb.ecb.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Milan Brankovic
 */
@Data
@Builder
public class AvailableCurrenciesDto {
    private List<String> to;
    private List<String> from;
}
