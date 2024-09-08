package com.rutaaprendizajewebflux.bootcamp.domain.exception;

public class CapabilitiesNotFoundException extends RuntimeException {
    public CapabilitiesNotFoundException (String message) {
        super(message);
    }
}
