package com.rutaaprendizajewebflux.bootcamp.domain.exception;

import com.rutaaprendizajewebflux.bootcamp.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class CapabilitiesNotFoundException extends CustomException {
    public CapabilitiesNotFoundException (String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
