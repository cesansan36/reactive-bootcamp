package com.rutaaprendizajewebflux.bootcamp.domain.exception;

import com.rutaaprendizajewebflux.bootcamp.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class BootcampAlreadyExistsException extends CustomException {

    public BootcampAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
