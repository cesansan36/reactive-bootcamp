package com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.entity.BootcampEntity;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampEntityMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements IBootcampPersistencePort {

    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

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

    @Override
    public Flux<Bootcamp> findAllPaginatedByField(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Query query = Query.query(Criteria.empty())
                .sort(Sort.by(sortDirection, sortBy))
                .limit(size)
                .offset((long) page * size);

        return r2dbcEntityTemplate
                .select(query, BootcampEntity.class)
                .map(bootcampEntityMapper::toModel);
    }

    @Override
    public Flux<Bootcamp> findAllByIds(Flux<Long> bootcampsIds) {
        return bootcampRepository
                .findAllById(bootcampsIds)
                .map(bootcampEntityMapper::toModel);
    }
}
