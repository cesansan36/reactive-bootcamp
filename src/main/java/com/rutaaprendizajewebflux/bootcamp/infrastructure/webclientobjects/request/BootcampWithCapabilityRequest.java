package com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BootcampWithCapabilityRequest {

    private Long bootcampId;
    private List<String> capabilitiesNames;
}
