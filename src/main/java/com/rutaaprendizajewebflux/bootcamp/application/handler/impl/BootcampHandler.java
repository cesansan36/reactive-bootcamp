package com.rutaaprendizajewebflux.bootcamp.application.handler.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.handler.IBootcampHandler;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Slf4j
public class BootcampHandler implements IBootcampHandler {

    private final ISaveBootcampServicePort saveBootcampServicePort;
    private final IBootcampRequestMapper bootcampRequestMapper;
    private final IBootcampResponseMapper bootcampResponseMapper;

    @Override
    public Mono<ServerResponse> save(ServerRequest serverRequest) {

        Mono<SaveBootcampRequest> request = serverRequest.bodyToMono(SaveBootcampRequest.class);

        Mono<BootcampResponse> response = request
                .map(bootcampRequestMapper::toModel)
                .flatMap(bootcampModel -> saveBootcampServicePort.save(Mono.just(bootcampModel)))
                .map(bootcampResponseMapper::toBootcampResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, BootcampResponse.class);
    }
}
