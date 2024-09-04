package com.rutaaprendizajewebflux.bootcamp.configuration.beanconfiguration.handler;

import com.rutaaprendizajewebflux.bootcamp.application.handler.IBootcampHandler;
import com.rutaaprendizajewebflux.bootcamp.application.handler.impl.BootcampHandler;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean
    public IBootcampHandler bootcampHandler(
            ISaveBootcampServicePort saveBootcampServicePort,
            IBootcampRequestMapper bootcampRequestMapper,
            IBootcampResponseMapper bootcampResponseMapper
    ) {
        return new BootcampHandler(saveBootcampServicePort, bootcampRequestMapper, bootcampResponseMapper);
    }
}
