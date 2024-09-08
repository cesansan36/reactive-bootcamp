package com.rutaaprendizajewebflux.bootcamp.domain.ports.out;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapabilityCommunicationPort {
    Mono<Bootcamp> asociateCapabilitiesWithBootcamp(Bootcamp bootcamp);

    Mono<Bootcamp> getCapabilitiesByBootcampId(Long id);

    Flux<Bootcamp> findPaginatedBootcampIdsByCapabilityAmount(int page, int size, String direction);
}
