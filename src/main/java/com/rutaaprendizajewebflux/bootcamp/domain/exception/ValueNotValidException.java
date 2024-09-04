package com.rutaaprendizajewebflux.bootcamp.domain.exception;

public class ValueNotValidException extends RuntimeException {

    public ValueNotValidException(String message) {
        super(message);
    }
}
