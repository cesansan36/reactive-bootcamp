package com.rutaaprendizajewebflux.bootcamp.infrastructure.repository;

import com.rutaaprendizajewebflux.bootcamp.infrastructure.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IBootcampRepository extends ReactiveCrudRepository<BootcampEntity, String> {

    Mono<BootcampEntity> findByName(String name);
}
