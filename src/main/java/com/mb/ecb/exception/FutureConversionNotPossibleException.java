package com.mb.ecb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Milan Brankovic
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Future conversion not possible")
public class FutureConversionNotPossibleException extends RuntimeException {
    public FutureConversionNotPossibleException(final String message) {
        super(message);
    }
}
