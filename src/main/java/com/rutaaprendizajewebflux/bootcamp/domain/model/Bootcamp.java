package com.rutaaprendizajewebflux.bootcamp.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bootcamp {

    private Long id;
    private String name;
    private String description;
    private List<Capability> capabilities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public Bootcamp(Long id, String name, String description, List<Capability> capabilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capabilities = capabilities;
    }

    public Bootcamp() {
    }

    public void removeDuplicatedCapabilitiesByName() {
        if (capabilities == null || capabilities.isEmpty()) {
            return;
        }

        Set<String> uniqueCapabilitiesNames = new HashSet<>();
        List<Capability> filteredCapabilities = new ArrayList<>();
        for (Capability capability : capabilities) {
            if (capability != null && capability.getName() != null && uniqueCapabilitiesNames.add(capability.getName())) {
                filteredCapabilities.add(capability);
            }
        }

        this.capabilities = filteredCapabilities;
    }

    public List<String> getCapabilitiesNames() {
        return capabilities
                .stream()
                .map(Capability::getName)
                .toList();
    }

    @Override
    public String toString() {
        return "Bootcamp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", capabilities=" + capabilities +
                '}';
    }
}
