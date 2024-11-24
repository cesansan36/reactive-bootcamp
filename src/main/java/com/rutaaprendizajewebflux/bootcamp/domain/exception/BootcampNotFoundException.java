package com.rutaaprendizajewebflux.bootcamp.domain.exception;

import com.rutaaprendizajewebflux.bootcamp.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class BootcampNotFoundException extends CustomException {

    public BootcampNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
