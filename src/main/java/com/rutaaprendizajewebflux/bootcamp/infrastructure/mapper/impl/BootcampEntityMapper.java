package com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.impl;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.entity.BootcampEntity;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampEntityMapper;

public class BootcampEntityMapper implements IBootcampEntityMapper {

    @Override
    public Bootcamp toModel(BootcampEntity bootcampEntity) {
        return new Bootcamp(
                bootcampEntity.getId(),
                bootcampEntity.getName(),
                bootcampEntity.getDescription(),
                null
        );
    }

    @Override
    public BootcampEntity toEntity(Bootcamp bootcamp) {
        return new BootcampEntity(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription()
        );
    }
}
