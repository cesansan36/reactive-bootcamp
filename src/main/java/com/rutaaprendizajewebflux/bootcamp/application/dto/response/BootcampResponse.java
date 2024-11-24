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
public class BootcampResponse {

    private Long id;
    private String name;
    private String description;
    private int totalCapabilities;
    private List<CapabilityInBootcampResponse> capabilities;
}
