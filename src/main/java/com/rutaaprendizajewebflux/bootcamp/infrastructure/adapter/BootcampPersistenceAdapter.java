package com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampEntityMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements IBootcampPersistencePort {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;

    @Override
    public Mono<Bootcamp> findByName(String name) {
        return bootcampRepository
                .findByName(name).map(bootcampEntityMapper::toModel);
    }

    @Override
    public Mono<Bootcamp> save(Mono<Bootcamp> bootcamp) {
        return bootcamp
                .map(bootcampEntityMapper::toEntity)
                .flatMap(bootcampRepository::save)
                .map(bootcampEntityMapper::toModel);
    }
}
