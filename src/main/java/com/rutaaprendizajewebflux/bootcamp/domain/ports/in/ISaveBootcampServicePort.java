package com.rutaaprendizajewebflux.bootcamp.domain.ports.in;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface ISaveBootcampServicePort {
    Mono<Bootcamp> save(Mono<Bootcamp> bootcamp);
}
