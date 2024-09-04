package com.rutaaprendizajewebflux.bootcamp.domain.exception;

public class BootcampAlreadyExistsException extends RuntimeException {

    public BootcampAlreadyExistsException(String message) {
        super(message);
    }
}
