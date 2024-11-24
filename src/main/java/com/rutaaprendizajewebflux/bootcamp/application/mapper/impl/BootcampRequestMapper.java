package com.rutaaprendizajewebflux.bootcamp.application.mapper.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;

import java.util.List;

public class BootcampRequestMapper implements IBootcampRequestMapper {

    @Override
    public Bootcamp toModel(SaveBootcampRequest saveBootcampRequest) {

        List<Capability> capabilities = saveBootcampRequest
                .getCapabilitiesNames()
                .stream()
                .map(cap -> new Capability(null, cap, null))
                .toList();

        return new Bootcamp(
                null,
                saveBootcampRequest.getName(),
                saveBootcampRequest.getDescription(),
                capabilities
        );
    }
}
