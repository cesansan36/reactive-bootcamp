package com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.impl;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Technology;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampWebClientDtoMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.request.BootcampWithCapabilityRequest;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.BootcampWithCapabilityResponse;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.CapabilityResponse;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.TechnologyResponse;

import java.util.List;

public class BootcampWebClientDtoMapper implements IBootcampWebClientDtoMapper {

    @Override
    public BootcampWithCapabilityRequest toRequest(Bootcamp bootcamp) {
        System.out.println("en el to rquest del webclietn");
        System.out.println(bootcamp);

        BootcampWithCapabilityRequest request = new BootcampWithCapabilityRequest();
        request.setBootcampId(bootcamp.getId());
        request.setCapabilitiesNames(bootcamp.getCapabilitiesNames());
        return request;
    }

    @Override
    public Bootcamp toModel(BootcampWithCapabilityResponse bootcampWithCapabilityResponse) {

        System.out.println("desde el to bottcamp model despues de consulta ========================");
        System.out.println(bootcampWithCapabilityResponse);

        List<Capability> capabilities = bootcampWithCapabilityResponse
                .getCapabilities()
                .stream()
                .map(this::toCapability)
                .toList();

        return new Bootcamp(
                bootcampWithCapabilityResponse.getBootcampId(),
                null,
                null,
                capabilities);
    }

    private Capability toCapability(CapabilityResponse capabilityResponse) {

        System.out.println("desde el to capability en el mapper ================");
        System.out.println(capabilityResponse);

        List<Technology> technologies = capabilityResponse
                .getTechnologies()
                .stream()
                .map(this::toTechnology)
                .toList();

        return new Capability(
                capabilityResponse.getCapabilityId(),
                capabilityResponse.getName(),
                technologies
        );
    }

    private Technology toTechnology(TechnologyResponse technologyResponse) {

        return new Technology(
                technologyResponse.getId(),
                technologyResponse.getName()
        );
    }
}