package com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampWebClientDtoMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.BootcampWithCapabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityWebClientAdapter implements ICapabilityCommunicationPort {

    private final WebClient webClient;
    private final IBootcampWebClientDtoMapper bootcampWebClientMapper;

    @Override
    public Mono<Bootcamp> asociateCapabilitiesWithBootcamp(Bootcamp bootcamp) {
        return webClient
                .post()
                .uri("/linked-bootcamp-capabilities")
                .bodyValue(bootcampWebClientMapper.toRequest(bootcamp))
                .retrieve()
                .onStatus( status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("No fue posible asociar las capacidades al bootcamp: " + error))))
                .bodyToMono(BootcampWithCapabilityResponse.class)
                .map(bootcampWebClientMapper::toModel)
                .onErrorResume(error-> Mono.error(new RuntimeException(error.getMessage())));
    }
}
