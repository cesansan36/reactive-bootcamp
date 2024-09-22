package com.rutaaprendizajewebflux.bootcamp.domain.usecase;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.BootcampNotFoundException;
import com.rutaaprendizajewebflux.bootcamp.domain.exception.CapabilitiesNotFoundException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.IReadBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_CAPABILITIES;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.ExceptionConstants.BOOTCAMP_NOT_FOUND_MESSAGE;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.ExceptionConstants.CAPABILITIES_NOT_FOUND_MESSAGE;

public class ReadBootcampUseCase implements IReadBootcampServicePort {

    private final ICapabilityCommunicationPort capabilityCommunicationPort;
    private final IBootcampPersistencePort bootcampPersistencePort;

    public ReadBootcampUseCase(ICapabilityCommunicationPort capabilityCommunicationPort, IBootcampPersistencePort bootcampPersistencePort) {
        this.capabilityCommunicationPort = capabilityCommunicationPort;
        this.bootcampPersistencePort = bootcampPersistencePort;
    }

    @Override
    public Flux<Bootcamp> findAllPaginated(int page, int size, String sortBy, String direction) {

        if(sortBy.equals(ORDER_BY_CAPABILITIES)) {
            return findPaginatedByCapabilityQuantity(page, size, direction);
        }
        else {
            return findPaginatedByField(page, size, sortBy, direction);
        }
    }

    private Flux<Bootcamp> findPaginatedByCapabilityQuantity(int page, int size, String direction) {
        Flux<Bootcamp> bootcampsIdWithCapabilities = capabilityCommunicationPort
                .findPaginatedBootcampIdsByCapabilityAmount(page, size, direction)
                .switchIfEmpty(Flux.error(new CapabilitiesNotFoundException(CAPABILITIES_NOT_FOUND_MESSAGE)));

        return bootcampsIdWithCapabilities
                .flatMap(bootcampWithCap -> bootcampPersistencePort
                        .findAllByIds(bootcampsIdWithCapabilities.map(Bootcamp::getId))
                        .switchIfEmpty(Flux.error(new BootcampNotFoundException(BOOTCAMP_NOT_FOUND_MESSAGE)))
                        .filter(bootcamp -> bootcamp.getId().equals(bootcampWithCap.getId()))
                        .map(bootcamp -> {
                            bootcamp.setCapabilities(bootcampWithCap.getCapabilities());
                            return bootcamp;
                        }));
    }

    private Flux<Bootcamp> findPaginatedByField(int page, int size, String sortBy, String direction) {
        return bootcampPersistencePort
                .findAllPaginatedByField(page, size, sortBy, direction)
                .switchIfEmpty(Flux.error(new BootcampNotFoundException(BOOTCAMP_NOT_FOUND_MESSAGE)))
                .flatMap(this::fillBootcampWithCapabilities);
    }

    private Mono<Bootcamp> fillBootcampWithCapabilities(Bootcamp bootcamp) {
        return capabilityCommunicationPort
                .getCapabilitiesByBootcampId(bootcamp.getId())
                .flatMap(capabilities -> {
                    if (capabilities.getCapabilities().isEmpty()) {
                        return Mono.error(new CapabilitiesNotFoundException(CAPABILITIES_NOT_FOUND_MESSAGE));
                    }
                    bootcamp.setCapabilities(capabilities.getCapabilities());
                    return Mono.just(bootcamp);
                })
                .switchIfEmpty(Mono.error(new CapabilitiesNotFoundException(CAPABILITIES_NOT_FOUND_MESSAGE)));
    }
}
