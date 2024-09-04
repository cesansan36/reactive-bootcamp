package com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.request.BootcampWithCapabilityRequest;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.BootcampWithCapabilityResponse;

public interface IBootcampWebClientDtoMapper {
    BootcampWithCapabilityRequest toRequest(Bootcamp bootcamp);

    Bootcamp toModel(BootcampWithCapabilityResponse bootcampWithCapabilityResponse);
}
