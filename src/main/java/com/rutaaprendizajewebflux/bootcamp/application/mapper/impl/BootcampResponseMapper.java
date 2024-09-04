package com.rutaaprendizajewebflux.bootcamp.application.mapper.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.CapabilityInBootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Technology;

import java.util.ArrayList;
import java.util.List;

public class BootcampResponseMapper implements IBootcampResponseMapper {

    @Override
    public BootcampResponse toBootcampResponse(Bootcamp bootcamp) {
        List<CapabilityInBootcampResponse> capabilityInBootcampResponses = new ArrayList<>();

        bootcamp.getCapabilities().forEach(capability -> {
            CapabilityInBootcampResponse capabilityInBootcampResponse = toCapabilityResponse(capability);
            capabilityInBootcampResponses.add(capabilityInBootcampResponse);
        });

        return new BootcampResponse(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                capabilityInBootcampResponses.size(),
                capabilityInBootcampResponses
        );
    }

    private CapabilityInBootcampResponse toCapabilityResponse(Capability capability) {
        List<TechnologyInCapabilityResponse> technologies = new ArrayList<>();

        capability.getTechnologies().forEach(technology -> {
            TechnologyInCapabilityResponse technologyInCapabilityResponse = toTechnologyResponse(technology);
            technologies.add(technologyInCapabilityResponse);
        });

        return new CapabilityInBootcampResponse(
                capability.getId(),
                capability.getName(),
                technologies.size(),
                technologies
        );
    }

    private TechnologyInCapabilityResponse toTechnologyResponse(Technology technology) {
        return new TechnologyInCapabilityResponse(
                technology.getId(),
                technology.getName()
        );
    }
}
