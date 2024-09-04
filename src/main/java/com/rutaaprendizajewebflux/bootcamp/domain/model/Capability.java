package com.rutaaprendizajewebflux.bootcamp.domain.model;

import java.util.List;

public class Capability {

    private Long id;
    private String name;
    private List<Technology> technologies;

    public Capability() {
    }

    public Capability(Long id, String name, List<Technology> technologies) {
        this.id = id;
        this.name = name;
        this.technologies = technologies;
    }

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

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "Capability{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", technologies=" + technologies +
                '}';
    }
}
