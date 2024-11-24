package com.rutaaprendizajewebflux.bootcamp.application.mapper;

import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;

public interface IBootcampResponseMapper {
    BootcampResponse toBootcampResponse(Bootcamp bootcamp);
}
