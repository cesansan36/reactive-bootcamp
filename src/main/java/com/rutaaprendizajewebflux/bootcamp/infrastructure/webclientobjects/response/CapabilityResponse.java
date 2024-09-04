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
public class CapabilityResponse {

    private Long capabilityId;
    private String name;
    private List<TechnologyResponse> technologies;

    @Override
    public String toString() {
        return "CapabilityResponse{" +
                "id=" + capabilityId +
                ", name='" + name + '\'' +
                ", technologies=" + technologies +
                '}';
    }
}