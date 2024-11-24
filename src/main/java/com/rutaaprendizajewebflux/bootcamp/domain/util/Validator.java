package com.rutaaprendizajewebflux.bootcamp.domain.util;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;

import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.MAX_CAPABILITIES_PER_BOOTCAMP;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.MIN_CAPABILITIES_PER_BOOTCAMP;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.ExceptionConstants.MAXIMUM_AMOUNT_CAPABILITIES_EXCEPTION_MESSAGE;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.ExceptionConstants.MINIMUM_AMOUNT_CAPABILITIES_EXCEPTION_MESSAGE;

public final class Validator {

    private Validator() {
    }

    public static Bootcamp validate(Bootcamp bootcamp) {
        bootcamp.removeDuplicatedCapabilitiesByName();
        validateMaximumAmountOfTechnologies(bootcamp);
        validateMinimumAmountOfTechnologies(bootcamp);

        return bootcamp;
    }

    private static void validateMinimumAmountOfTechnologies(Bootcamp bootcamp) {
        if (bootcamp.getCapabilities().size() < MIN_CAPABILITIES_PER_BOOTCAMP) {
            throw new ValueNotValidException(MINIMUM_AMOUNT_CAPABILITIES_EXCEPTION_MESSAGE.formatted(MIN_CAPABILITIES_PER_BOOTCAMP));
        }
    }

    private static void validateMaximumAmountOfTechnologies(Bootcamp bootcamp) {
        if (bootcamp.getCapabilities().size() > DomainConstants.MAX_CAPABILITIES_PER_BOOTCAMP) {
            throw new ValueNotValidException(MAXIMUM_AMOUNT_CAPABILITIES_EXCEPTION_MESSAGE.formatted(MAX_CAPABILITIES_PER_BOOTCAMP));
        }
    }
}
