package com.rutaaprendizajewebflux.bootcamp.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityInBootcampResponse {

    private Long id;
    private String name;
    private int totalTechnologies;
    private List<TechnologyInCapabilityResponse> technologies;
}
