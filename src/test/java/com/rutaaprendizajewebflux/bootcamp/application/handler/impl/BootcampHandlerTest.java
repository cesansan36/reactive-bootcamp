package com.rutaaprendizajewebflux.bootcamp.application.handler.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.CapabilityInBootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampHandlerTest {

    @Mock
    private ISaveBootcampServicePort saveBootcampServicePort;
    @Mock
    private IBootcampRequestMapper bootcampRequestMapper;
    @Mock
    private IBootcampResponseMapper bootcampResponseMapper;

    @InjectMocks
    private BootcampHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction()).build();
    }

    private RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .POST("/bootcamp", handler::save)
                .build();
    }

    @Test
    void testSave() {
        List<String> capabilitiesNames = List.of("Capacidad 1", "Capacidad 2");
        SaveBootcampRequest saveRequest = new SaveBootcampRequest("Bootcamp 1", "Descripción 1", capabilitiesNames);
        List<CapabilityInBootcampResponse> capabilities = List.of(new CapabilityInBootcampResponse(1L, "Capacidad 1", 0, List.of()), new CapabilityInBootcampResponse(2L, "Capacidad 2", 0, List.of()));
        BootcampResponse response = new BootcampResponse(1L, "Bootcamp 1", "Descripción 1", 2, capabilities);

        when(bootcampRequestMapper.toModel(any(SaveBootcampRequest.class)))
                .thenReturn(new Bootcamp(null, "Bootcamp 1", "Descripción 1", new ArrayList<>()));
        when(saveBootcampServicePort.save(any(Mono.class)))
                .thenReturn(Mono.just(new Bootcamp(1L, "Bootcamp 1", "Descripción 1", new ArrayList<>())));
        when(bootcampResponseMapper.toBootcampResponse(any(Bootcamp.class)))
                .thenReturn(response);

        webTestClient.post()
                .uri("/bootcamp")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(saveRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BootcampResponse.class)
                .value(bootcampResponse -> assertThat(bootcampResponse).usingRecursiveComparison().isEqualTo(response));
    }
}