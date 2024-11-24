package com.rutaaprendizajewebflux.bootcamp.configuration.beanconfiguration.port.primary;

import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.IReadBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.domain.usecase.ReadBootcampUseCase;
import com.rutaaprendizajewebflux.bootcamp.domain.usecase.SaveBootcampUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class ServiceConfig {

    @Bean
    public ISaveBootcampServicePort saveBootcampServicePort(
            IBootcampPersistencePort bootcampPersistencePort,
            ICapabilityCommunicationPort capabilityCommunicationPort,
            TransactionalOperator transactionalOperator
    ) {
        return new SaveBootcampUseCase(bootcampPersistencePort, capabilityCommunicationPort, transactionalOperator);
    }

    @Bean
    public IReadBootcampServicePort readBootcampServicePort(
            ICapabilityCommunicationPort capabilityCommunicationPort,
            IBootcampPersistencePort bootcampPersistencePort
    ) {
        return new ReadBootcampUseCase(capabilityCommunicationPort, bootcampPersistencePort);
    }
}
