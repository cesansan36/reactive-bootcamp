package com.rutaaprendizajewebflux.bootcamp.domain.usecase;

import com.rutaaprendizajewebflux.bootcamp.domain.exception.BootcampAlreadyExistsException;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Capability;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.IBootcampPersistencePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.domain.util.Validator;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .flatMap(bootcamp -> {
                    List<Capability> capabilities = bootcamp.getCapabilities();

                    return saveBootcamp(bootcamp)
                            .map(savedBootcamp -> {
                                savedBootcamp.setCapabilities(capabilities);
                                return savedBootcamp;
                            })
                            .flatMap(this::asociateCapabilitiesWithBootcamp);

                })
                .as(transactionalOperator::transactional);
    }

    private Bootcamp validate(Bootcamp bootcamp) {
        return Validator.validate(bootcamp);
    }

    private Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return bootcampPersistencePort.save(Mono.just(bootcamp));
    }

    Mono<Bootcamp> asociateCapabilitiesWithBootcamp(Bootcamp bootcamp) {
        return capabilityCommunicationPort.asociateCapabilitiesWithBootcamp(bootcamp)
                .map(booWithRelation -> {
                    System.out.println("after asociation ============================");
                    System.out.println(booWithRelation);
                    booWithRelation.setName(bootcamp.getName());
                    booWithRelation.setDescription(bootcamp.getDescription());
                    return booWithRelation;
                });
    }
}
