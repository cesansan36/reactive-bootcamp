package com.rutaaprendizajewebflux.bootcamp.configuration.beanconfiguration.port.secondary;

import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter.BootcampPersistenceAdapter;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter.CapabilityWebClientAdapter;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampEntityMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampWebClientDtoMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.repository.IBootcampRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AdapterConfig {

    @Bean
    public ICapabilityCommunicationPort capabilityCommunicationPort(
            WebClient webClient,
            IBootcampWebClientDtoMapper bootcampWebClientMapper
    ) {
        return new CapabilityWebClientAdapter(webClient, bootcampWebClientMapper);
    }

    @Bean
    public IBootcampPersistencePort bootcampPersistencePort(
            IBootcampRepository bootcampRepository,
            IBootcampEntityMapper bootcampEntityMapper
    ) {
        return new BootcampPersistenceAdapter(bootcampRepository, bootcampEntityMapper);
    }
}
