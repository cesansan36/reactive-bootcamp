package com.rutaaprendizajewebflux.bootcamp.configuration.beanconfiguration.mapper;

import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.impl.BootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.impl.BootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampEntityMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampWebClientDtoMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.impl.BootcampEntityMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.impl.BootcampWebClientDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public IBootcampWebClientDtoMapper bootcampWebClientDtoMapper() {
        return new BootcampWebClientDtoMapper();
    }

    @Bean
    public IBootcampEntityMapper bootcampEntityMapper() {
        return new BootcampEntityMapper();
    }

    @Bean
    public IBootcampRequestMapper bootcampRequestMapper() {
        return new BootcampRequestMapper();
    }

    @Bean
    public IBootcampResponseMapper bootcampResponseMapper() {
        return new BootcampResponseMapper();
    }
}
