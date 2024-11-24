package com.rutaaprendizajewebflux.bootcamp.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BootcampEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
