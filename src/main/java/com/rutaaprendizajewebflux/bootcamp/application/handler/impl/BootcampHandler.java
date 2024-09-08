package com.rutaaprendizajewebflux.bootcamp.application.handler.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.handler.IBootcampHandler;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.IReadBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_CAPABILITIES;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_DESCRIPTION;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_ID;
import static com.rutaaprendizajewebflux.bootcamp.domain.util.DomainConstants.ORDER_BY_NAME;
import static com.rutaaprendizajewebflux.bootcamp.util.Validator.validateWholeNumber;


@RequiredArgsConstructor
@Slf4j
public class BootcampHandler implements IBootcampHandler {

    private final ISaveBootcampServicePort saveBootcampServicePort;
    private final IReadBootcampServicePort readBootcampServicePort;
    private final IBootcampRequestMapper bootcampRequestMapper;
    private final IBootcampResponseMapper bootcampResponseMapper;

    public static final List<String> ALLOWED_ORDER_BY_VALUES = List.of(ORDER_BY_ID, ORDER_BY_NAME, ORDER_BY_DESCRIPTION, ORDER_BY_CAPABILITIES);
    public static final List<String> ALLOWED_SORT_DIRECTIONS = List.of("ASC", "DESC");
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 3;
    public static final String DEFAULT_SORT_BY = ORDER_BY_NAME;
    public static final String DEFAULT_SORT_DIRECTION = Sort.Direction.ASC.name();

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

    @Override
    public Mono<ServerResponse> findAllPaginated(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(num -> validateWholeNumber(num, DEFAULT_PAGE)).orElse(DEFAULT_PAGE);
        int size = serverRequest.queryParam("size").map(num -> validateWholeNumber(num, DEFAULT_SIZE)).orElse(DEFAULT_SIZE);
        String sortBy = serverRequest.queryParam("sortBy").filter(ALLOWED_ORDER_BY_VALUES::contains).orElse(DEFAULT_SORT_BY);
        String direction = serverRequest.queryParam("direction").filter(ALLOWED_SORT_DIRECTIONS::contains).orElse(DEFAULT_SORT_DIRECTION);

        Mono<List<BootcampResponse>> response = readBootcampServicePort
                .finadAllPaginated(page, size, sortBy, direction)
                .map(bootcampResponseMapper::toBootcampResponse)
                .collectSortedList((bootcamp1, bootcamp2) -> {
                    int comparisonResult;
                    if(sortBy.equalsIgnoreCase("capabilities")) {
                        comparisonResult = Integer.compare(bootcamp1.getCapabilities().size(), bootcamp2.getCapabilities().size());
                    } else if (sortBy.equalsIgnoreCase("name")) {
                        comparisonResult = bootcamp1.getName().compareToIgnoreCase(bootcamp2.getName());
                    }
                    else {
                        comparisonResult = bootcamp1.getId().compareTo(bootcamp2.getId());
                    }
                    return direction.equalsIgnoreCase("ASC") ? comparisonResult : -comparisonResult;
                });

        return ServerResponse
                .ok()
                .body(response, BootcampResponse.class);
    }
}
