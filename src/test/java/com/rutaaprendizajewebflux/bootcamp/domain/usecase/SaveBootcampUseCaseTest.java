package com.rutaaprendizajewebflux.bootcamp.domain.usecase;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.BootcampAlreadyExistsException;
import com.rutaaprendizajewebflux.bootcamp.domain.exception.ValueNotValidException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveBootcampUseCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private ICapabilityCommunicationPort capabilityCommunicationPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    private SaveBootcampUseCase saveBootcampUseCase;

    @BeforeEach
    void setUp() {
        saveBootcampUseCase = new SaveBootcampUseCase(bootcampPersistencePort, capabilityCommunicationPort, transactionalOperator);
    }

    @Test
    void save_SuccessfulScenario() {
        // Given
        List<Capability> capabilities = List.of(new Capability(1L, "Cap 1", List.of()), new Capability(2L, "Cap 2", List.of()), new Capability(3L, "Cap 3", List.of()));
        Bootcamp bootcamp = new Bootcamp(1L, "Fullstack Development", "Fullstack Bootcamp", capabilities);

        when(bootcampPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(bootcampPersistencePort.save(any(Mono.class))).thenReturn(Mono.just(bootcamp));
        when(capabilityCommunicationPort.asociateCapabilitiesWithBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcamp));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<Bootcamp> result = saveBootcampUseCase.save(Mono.just(bootcamp));

        // Then
        StepVerifier.create(result)
                .expectNextMatches(savedBootcamp -> savedBootcamp.getName().equals("Fullstack Development") &&
                        savedBootcamp.getCapabilities().size() == 3)
                .verifyComplete();

        verify(bootcampPersistencePort, times(1)).save(any(Mono.class));
        verify(capabilityCommunicationPort, times(1)).asociateCapabilitiesWithBootcamp(any(Bootcamp.class));
    }

    @Test
    void save_BootcampAlreadyExists() {
        // Given
        List<Capability> capabilities = List.of(new Capability(1L, "Cap 1", List.of()), new Capability(2L, "Cap 2", List.of()), new Capability(3L, "Cap 3", List.of()));
        Bootcamp bootcamp = new Bootcamp(1L, "Fullstack Development", "Fullstack Bootcamp", capabilities);
        Bootcamp existingBootcamp = new Bootcamp(1L, "Fullstack Development", "Fullstack Bootcamp", capabilities);

        // Mock findByName para devolver un Mono indicando que el bootcamp ya existe
        when(bootcampPersistencePort.findByName(anyString())).thenReturn(Mono.just(existingBootcamp));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<Bootcamp> result = saveBootcampUseCase.save(Mono.just(bootcamp));

        // Then
        StepVerifier.create(result)
                .expectError(BootcampAlreadyExistsException.class)
                .verify();

        // Verificar que el método save no se llame, ya que el bootcamp ya existe
        verify(bootcampPersistencePort, never()).save(any(Mono.class));
        verify(capabilityCommunicationPort, never()).asociateCapabilitiesWithBootcamp(any(Bootcamp.class));
    }


    @Test
    void save_AssociationWithCapabilitiesFails() {
        // Given
        List<Capability> capabilities = List.of(new Capability(1L, "Cap 1", List.of()), new Capability(2L, "Cap 2", List.of()), new Capability(3L, "Cap 3", List.of()));
        Bootcamp bootcamp = new Bootcamp(1L, "Fullstack Development", "Fullstack Bootcamp", capabilities);

        when(bootcampPersistencePort.findByName(anyString())).thenReturn(Mono.empty());
        when(bootcampPersistencePort.save(any(Mono.class))).thenReturn(Mono.just(bootcamp));
        when(capabilityCommunicationPort.asociateCapabilitiesWithBootcamp(any(Bootcamp.class))).thenReturn(Mono.error(new RuntimeException("Failed to associate technologies")));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<Bootcamp> result = saveBootcampUseCase.save(Mono.just(bootcamp));

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampPersistencePort, times(1)).save(any(Mono.class));
        verify(capabilityCommunicationPort, times(1)).asociateCapabilitiesWithBootcamp(any(Bootcamp.class));
    }

    @Test
    void save_ValidationFailsDueToInvalidCapabilities() {
        // Given
        List<Capability> invalidCapabilities = List.of();
        Bootcamp invalidBootcamp = new Bootcamp(1L, "Fullstack Development", "Fullstack Bootcamp", invalidCapabilities);

        // Cuando la validación falla en el método validate, no se llega a los pasos siguientes.
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Mono<Bootcamp> result = saveBootcampUseCase.save(Mono.just(invalidBootcamp));

        // Then
        StepVerifier.create(result)
                .expectError(ValueNotValidException.class)
                .verify();

        verify(bootcampPersistencePort, never()).save(any(Mono.class));
        verify(capabilityCommunicationPort, never()).asociateCapabilitiesWithBootcamp(any(Bootcamp.class));
    }
}