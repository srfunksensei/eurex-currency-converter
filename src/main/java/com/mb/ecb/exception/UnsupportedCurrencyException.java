package com.mb.ecb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Milan Brankovic
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unsupported currency")
public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(final String message) {
        super(message);
    }
}
