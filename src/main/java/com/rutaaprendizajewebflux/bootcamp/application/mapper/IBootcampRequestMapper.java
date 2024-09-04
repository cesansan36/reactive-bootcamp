package com.rutaaprendizajewebflux.bootcamp.application.mapper;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;

public interface IBootcampRequestMapper {
    Bootcamp toModel(SaveBootcampRequest saveBootcampRequest);
}
