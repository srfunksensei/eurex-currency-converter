package com.mb.ecb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Milan Brankovic
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Exchange rate data unavailable")
public class ExchangeRateUnavailableException extends RuntimeException {
    public ExchangeRateUnavailableException(final String message) {
        super(message);
    }
}
