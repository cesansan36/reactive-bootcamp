package com.rutaaprendizajewebflux.bootcamp.domain.usecase;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.BootcampNotFoundException;
import com.rutaaprendizajewebflux.bootcamp.domain.exception.CapabilitiesNotFoundException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_CAPABILITIES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadBootcampUseCaseTest {

    @Mock
    private ICapabilityCommunicationPort capabilityCommunicationPort;
    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @InjectMocks
    private ReadBootcampUseCase readBootcampUseCase;

    @Test
    void findAllPaginated_WithCapabilities_Success() {
        Bootcamp bootcamp = new Bootcamp(1L, "Backend Development bootcamp", "Backend bootcamp", List.of());

        when(capabilityCommunicationPort.findPaginatedBootcampIdsByCapabilityAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.just(bootcamp));
        when(bootcampPersistencePort.findAllByIds(any(Flux.class)))
                .thenReturn(Flux.just(bootcamp));

        Flux<Bootcamp> result = readBootcampUseCase.findAllPaginated(0, 10, ORDER_BY_CAPABILITIES, "asc");

        StepVerifier.create(result)
                .expectNextMatches(capa -> capa.getName().equals("Backend Development bootcamp"))
                .verifyComplete();
    }

    @Test
    void findAllPaginated_WithCapabilities_CapabilitiesNotFoundException() {
        when(capabilityCommunicationPort.findPaginatedBootcampIdsByCapabilityAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.empty());

        when(bootcampPersistencePort.findAllByIds(any(Flux.class))).thenReturn(Flux.empty());

        Flux<Bootcamp> result = readBootcampUseCase.findAllPaginated(0, 10, ORDER_BY_CAPABILITIES, "asc");

        StepVerifier.create(result)
                .expectError(CapabilitiesNotFoundException.class)
                .verify();
    }

    @Test
    void findAllPaginated_ByField_Success() {
        Bootcamp capability = new Bootcamp(1L, "Backend Development bootcamp", "Backend bootcamp", List.of());

        when(bootcampPersistencePort.findAllPaginatedByField(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.just(capability));
        when(capabilityCommunicationPort.getCapabilitiesByBootcampId(anyLong()))
                .thenReturn(Mono.just(new Bootcamp(1L, null, null, List.of(new Capability(1L, "Backend", List.of())))));

        Flux<Bootcamp> result = readBootcampUseCase.findAllPaginated(0, 10, "name", "asc");

        StepVerifier.create(result)
                .expectNextMatches(capa -> capa.getCapabilities().size() == 1)
                .verifyComplete();
    }

    @Test
    void findAllPaginated_ByField_BootcampNotFoundException() {
        when(bootcampPersistencePort.findAllPaginatedByField(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Flux.empty());

        Flux<Bootcamp> result = readBootcampUseCase.findAllPaginated(0, 10, "name", "asc");

        StepVerifier.create(result)
                .expectError(BootcampNotFoundException.class)
                .verify();
    }

    @Test
    void findPaginatedByCapabilityQuantity_BootcampNotFoundException() {
        Bootcamp capability = new Bootcamp(1L, "Backend Development", "Backend Capability", List.of());

        when(capabilityCommunicationPort.findPaginatedBootcampIdsByCapabilityAmount(anyInt(), anyInt(), anyString()))
                .thenReturn(Flux.just(capability));
        when(bootcampPersistencePort.findAllByIds(any(Flux.class)))
                .thenReturn(Flux.empty());

        Flux<Bootcamp> result = readBootcampUseCase.findAllPaginated(0, 10, ORDER_BY_CAPABILITIES, "asc");

        StepVerifier.create(result)
                .expectError(BootcampNotFoundException.class)
                .verify();
    }

}