package com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.entity.BootcampEntity;

public interface IBootcampEntityMapper {
    Bootcamp toModel(BootcampEntity bootcampEntity);

    BootcampEntity toEntity(Bootcamp bootcamp);
}
