package com.rutaaprendizajewebflux.bootcamp.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {
    Mono<ServerResponse> save(ServerRequest serverRequest);
}
