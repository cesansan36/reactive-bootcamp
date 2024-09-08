package com.rutaaprendizajewebflux.bootcamp.domain.ports.in;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Flux;

public interface IReadBootcampServicePort {
    Flux<Bootcamp> finadAllPaginated(int page, int size, String sortBy, String direction);
}
