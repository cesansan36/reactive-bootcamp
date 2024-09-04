package com.rutaaprendizajewebflux.bootcamp.domain.ports.out;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {

    Mono<Bootcamp> findByName(String name);

    Mono<Bootcamp> save(Mono<Bootcamp> bootcamp);
}
