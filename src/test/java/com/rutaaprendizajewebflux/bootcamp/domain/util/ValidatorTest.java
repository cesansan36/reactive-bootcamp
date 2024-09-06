package com.rutaaprendizajewebflux.bootcamp.domain.util;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Technology;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    @Test
    void shouldPassValidationWithValidCapabilities() {
        // Given
        List<Capability> capabilities = List.of(
                new Capability(1L, "Cap 1", List.of(new Technology(1L, "Java"))),
                new Capability(2L, "Cap 2", List.of(new Technology(1L, "Java"), new Technology(2L, "Python"))),
                new Capability(3L, "Cap 3", List.of(new Technology(1L, "Java"), new Technology(2L, "C#"), new Technology(3L, "javascript")))
        );
        Bootcamp bootcamp = new Bootcamp(1L, "Backend Development", "Backend 101", capabilities);

        // When
        Bootcamp result = Validator.validate(bootcamp);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCapabilities()).hasSize(3);
    }

    @Test
    void shouldThrowExceptionWhenCapabilitiesAreLessThanMinimum() {
        // Given
        List<Capability> capabilities = List.of();
        Bootcamp bootcamp = new Bootcamp(1L, "Backend Development", "Backend 101", capabilities);

        // When / Then
        assertThrows(ValueNotValidException.class, () -> Validator.validate(bootcamp));
    }

    @Test
    void shouldThrowExceptionWhenTechnologiesExceedMaximum() {
        // Given
        List<Capability> capabilities = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            capabilities.add(new Capability((long) i, "Cap" + i, List.of()));
        }
        Bootcamp bootcamp = new Bootcamp(1L, "Backend Development", "Backend 101", capabilities);

        // When / Then
        assertThrows(ValueNotValidException.class, () -> Validator.validate(bootcamp));
    }

    @Test
    void shouldRemoveDuplicatedCapabilitiesByName() {
        // Given
        List<Capability> capabilities = List.of(
                new Capability(1L, "Cap 1", new ArrayList<>()),
                new Capability(2L, "Cap 2", new ArrayList<>()),  // Duplicate
                new Capability(2L, "Cap 1", new ArrayList<>()),
                new Capability(3L, "Cap 3", new ArrayList<>())
        );
        Bootcamp bootcamp = new Bootcamp(1L, "Backend Development", "Backend 101", capabilities);

        // When
        Bootcamp result = Validator.validate(bootcamp);

        // Then
        assertThat(result.getCapabilities()).hasSize(3);
        assertThat(result.getCapabilitiesNames()).containsExactlyInAnyOrder("Cap 1", "Cap 2", "Cap 3");
    }
}