package com.rutaaprendizajewebflux.bootcamp.domain.exception;

import com.rutaaprendizajewebflux.bootcamp.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class ValueNotValidException extends CustomException {

    public ValueNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
