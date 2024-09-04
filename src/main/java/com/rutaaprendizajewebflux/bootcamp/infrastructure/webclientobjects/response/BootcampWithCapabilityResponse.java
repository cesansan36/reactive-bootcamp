package com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BootcampWithCapabilityResponse {

    private Long bootcampId;
    private List<CapabilityResponse> capabilities;

    @Override
    public String toString() {
        return "BootcampWithCapabilityResponse{" +
                "bootcampId=" + bootcampId +
                ", capabilities=" + capabilities +
                '}';
    }
}
