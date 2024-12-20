package com.rutaaprendizajewebflux.bootcamp.domain.usecase;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.BootcampAlreadyExistsException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.domain.util.Validator;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

public class SaveBootcampUseCase implements ISaveBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final ICapabilityCommunicationPort capabilityCommunicationPort;
    private final TransactionalOperator transactionalOperator;

    public SaveBootcampUseCase(IBootcampPersistencePort bootcampPersistencePort, ICapabilityCommunicationPort capabilityCommunicationPort, TransactionalOperator transactionalOperator) {
        this.bootcampPersistencePort = bootcampPersistencePort;
        this.capabilityCommunicationPort = capabilityCommunicationPort;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Bootcamp> save(Mono<Bootcamp> bootcampMono) {
        return bootcampMono
                .map(this::validate)
                .flatMap(bootcamp -> bootcampPersistencePort.findByName(bootcamp.getName())
                        .hasElement()
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                            ? Mono.error(new BootcampAlreadyExistsException("El bootcamp ya existe"))
                            : Mono.just(bootcamp)))
                .flatMap(bootcamp -> saveBootcamp(bootcamp)
                        .map(savedBootcamp -> {
                            savedBootcamp.setCapabilities(bootcamp.getCapabilities());
                            return savedBootcamp;
                        })
                        .flatMap(this::asociateCapabilitiesWithBootcamp))
                .as(transactionalOperator::transactional);
    }

    private Bootcamp validate(Bootcamp bootcamp) {
        return Validator.validate(bootcamp);
    }

    private Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return bootcampPersistencePort.save(Mono.just(bootcamp));
    }

    private Mono<Bootcamp> asociateCapabilitiesWithBootcamp(Bootcamp bootcamp) {
        return capabilityCommunicationPort.asociateCapabilitiesWithBootcamp(bootcamp)
                .map(booWithRelation -> {
                    booWithRelation.setName(bootcamp.getName());
                    booWithRelation.setDescription(bootcamp.getDescription());
                    return booWithRelation;
                });
    }
}
