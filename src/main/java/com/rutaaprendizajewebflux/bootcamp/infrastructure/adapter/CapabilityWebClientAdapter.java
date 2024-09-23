package com.rutaaprendizajewebflux.bootcamp.infrastructure.adapter;

import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.out.ICapabilityCommunicationPort;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.exception.LinkingProcessException;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.mapper.IBootcampWebClientDtoMapper;
import com.rutaaprendizajewebflux.bootcamp.infrastructure.webclientobjects.response.BootcampWithCapabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
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
                                .flatMap(error -> Mono.error(new LinkingProcessException("No fue posible asociar las capacidades al bootcamp: " + error))))
                .bodyToMono(BootcampWithCapabilityResponse.class)
                .map(bootcampWebClientMapper::toModel)
                .onErrorResume(error-> Mono.error(new LinkingProcessException(error.getMessage())));
    }

    @Override
    public Mono<Bootcamp> getCapabilitiesByBootcampId(Long id) {
        return webClient
                .get()
                .uri("/linked-bootcamp-capabilities/{bootcampId}", id)
                .retrieve()
                .onStatus( status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new LinkingProcessException(errorBody))))
                .bodyToMono(BootcampWithCapabilityResponse.class)
                .map(bootcampWebClientMapper::toModel)
                .onErrorResume(Exception.class, ex -> Mono.error(new LinkingProcessException(ex.getMessage())));
    }

    @Override
    public Flux<Bootcamp> findPaginatedBootcampIdsByCapabilityAmount(int page, int size, String direction) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/linked-bootcamp-capabilities")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("direction", direction)
                        .build())
                .retrieve()
                .onStatus( status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new LinkingProcessException(errorBody))))
                .bodyToFlux(BootcampWithCapabilityResponse.class)
                .map(bootcampWebClientMapper::toModel)
                .onErrorResume(Exception.class, ex -> Mono.error(new LinkingProcessException(ex.getMessage())));
    }
}
