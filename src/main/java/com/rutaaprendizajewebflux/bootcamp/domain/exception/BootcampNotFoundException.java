package com.rutaaprendizajewebflux.bootcamp.domain.exception;

public class BootcampNotFoundException extends RuntimeException {

    public BootcampNotFoundException(String message) {
        super(message);
    }
}
