package com.mb.ecb.web;

import com.mb.ecb.dto.AvailableCurrenciesDto;
import com.mb.ecb.dto.ExchangeDto;
import com.mb.ecb.dto.ExchangeResponseDto;
import com.mb.ecb.service.ExchangeMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Milan Brankovic
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/ecb")
@Api(tags = "ExchangeMoney")
public class EcbExchangeMoneyResource {

    private final ExchangeMoneyService exchangeMoneyService;

    @GetMapping("/available-currencies")
    @ApiOperation(value = "Get available currencies")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Available currencies")
    })
    public ResponseEntity<AvailableCurrenciesDto> getAvailableCurrencies() {
        final AvailableCurrenciesDto availableCurrenciesDto = AvailableCurrenciesDto.builder()
                .from(exchangeMoneyService.getFromCurrencies())
                .to(exchangeMoneyService.getToCurrencies())
                .build();
        return ResponseEntity.ok(availableCurrenciesDto);
    }

    @PostMapping("/exchange")
    @ApiOperation(value = "Get conversion amount")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Conversion amount")
    })
    public ResponseEntity<ExchangeResponseDto> exchange(@RequestBody @Valid ExchangeDto dto) {
        final ExchangeResponseDto exchangeResponseDto = exchangeMoneyService.exchange(dto);
        return ResponseEntity.ok(exchangeResponseDto);
    }

}
