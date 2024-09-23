package com.rutaaprendizajewebflux.bootcamp.infrastructure.exception;

import com.rutaaprendizajewebflux.bootcamp.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class LinkingProcessException extends CustomException {

    public LinkingProcessException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
