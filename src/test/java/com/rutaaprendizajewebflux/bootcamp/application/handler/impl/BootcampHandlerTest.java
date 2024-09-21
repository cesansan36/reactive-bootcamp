package com.rutaaprendizajewebflux.bootcamp.application.handler.impl;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.CapabilityInBootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampRequestMapper;
import com.rutaaprendizajewebflux.bootcamp.application.mapper.IBootcampResponseMapper;
import com.rutaaprendizajewebflux.bootcamp.domain.model.Bootcamp;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.IReadBootcampServicePort;
import com.rutaaprendizajewebflux.bootcamp.domain.ports.in.ISaveBootcampServicePort;
import org.assertj.core.api.AssertionsForClassTypes;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.rutaaprendizajewebflux.bootcamp.application.handler.impl.BootcampHandler.DEFAULT_PAGE;
import static com.rutaaprendizajewebflux.bootcamp.application.handler.impl.BootcampHandler.DEFAULT_SIZE;
import static com.rutaaprendizajewebflux.bootcamp.application.handler.impl.BootcampHandler.DEFAULT_SORT_BY;
import static com.rutaaprendizajewebflux.bootcamp.application.handler.impl.BootcampHandler.DEFAULT_SORT_DIRECTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampHandlerTest {

    @Mock
    private ISaveBootcampServicePort saveBootcampServicePort;
    @Mock
    private IReadBootcampServicePort readBootcampServicePort;
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
                .GET("/bootcamp", handler::findAllPaginated)
                .build();
    }

    @Test
    void save() {
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

    @Test
    void testFindAllPaginated_DefaultPagination() {
        Bootcamp bootcamp1 = new Bootcamp(1L, "Bootcamp 1", "Description 1", new ArrayList<>());
        Bootcamp bootcamp2 = new Bootcamp(2L, "Bootcamp 2", "Description 2", new ArrayList<>());
        Flux<Bootcamp> bootcampModels = Flux.just(bootcamp1, bootcamp2);

        when(readBootcampServicePort.findAllPaginated(0, 5, "name", "ASC"))
                .thenReturn(bootcampModels);

        when(bootcampResponseMapper.toBootcampResponse(any(Bootcamp.class)))
                .thenAnswer(invocation -> {
                    Bootcamp model = invocation.getArgument(0);
                    return new BootcampResponse(model.getId(), model.getName(), model.getDescription(), 0, new ArrayList<>());
                });

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/bootcamp")
                        .queryParam("page", 0)
                        .queryParam("size", 5)
                        .queryParam("sortBy", "name")
                        .queryParam("direction", "ASC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampResponse.class)
                .value(bootcamps -> {
                    AssertionsForClassTypes.assertThat(bootcamps.size()).isEqualTo(2);
                    AssertionsForClassTypes.assertThat(bootcamps.get(0).getName()).isEqualTo("Bootcamp 1");
                    AssertionsForClassTypes.assertThat(bootcamps.get(1).getName()).isEqualTo("Bootcamp 2");
                });
    }

    @Test
    void testFindAllPaginated_CustomPaginationAndSorting() {
        Bootcamp model1 = new Bootcamp(1L, "Bootcamp A", "Description A", new ArrayList<>());
        Bootcamp model2 = new Bootcamp(2L, "Bootcamp B", "Description B", new ArrayList<>());
        BootcampResponse response1 = new BootcampResponse(1L, "Bootcamp A", "Description A", 0, new ArrayList<>());
        BootcampResponse response2 = new BootcampResponse(2L, "Bootcamp B", "Description B", 0, new ArrayList<>());

        when(readBootcampServicePort.findAllPaginated(1, 10, "id", "DESC"))
                .thenReturn(Flux.just(model2, model1));
        when(bootcampResponseMapper.toBootcampResponse(model1))
                .thenReturn(response1);
        when(bootcampResponseMapper.toBootcampResponse(model2))
                .thenReturn(response2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/bootcamp")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("sortBy", "id")
                        .queryParam("direction", "DESC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampResponse.class)
                .value(responses -> {
                    List<BootcampResponse> responsesList = new ArrayList<>(responses);
                    AssertionsForClassTypes.assertThat(responsesList.get(0)).usingRecursiveComparison().isEqualTo(response2);
                    AssertionsForClassTypes.assertThat(responsesList.get(1)).usingRecursiveComparison().isEqualTo(response1);
                });
    }

    @Test
    void testFindAllPaginated_SortByCapabilities() {
        String sortBy = "capabilities";
        String direction = "ASC";
        List<CapabilityInBootcampResponse> capabilities1 = List.of(new CapabilityInBootcampResponse(1L, "Cap 1", 0, new ArrayList<>()));
        List<CapabilityInBootcampResponse> capabilities2 = List.of(new CapabilityInBootcampResponse(2L, "Cap 2", 0, new ArrayList<>()), new CapabilityInBootcampResponse(3L, "Cap 3", 0, new ArrayList<>()));

        Bootcamp model1 = new Bootcamp(1L, "Bootcamp A", "Description A", new ArrayList<>());
        Bootcamp model2 = new Bootcamp(2L, "Bootcamp B", "Description B", new ArrayList<>());
        BootcampResponse response1 = new BootcampResponse(1L, "Bootcamp A", "Description A", 1, capabilities1);
        BootcampResponse response2 = new BootcampResponse(2L, "Bootcamp B", "Description B", 2, capabilities2);

        when(readBootcampServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, sortBy, direction))
                .thenReturn(Flux.just(model1, model2));
        when(bootcampResponseMapper.toBootcampResponse(model1))
                .thenReturn(response1);
        when(bootcampResponseMapper.toBootcampResponse(model2))
                .thenReturn(response2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/bootcamp")
                        .queryParam("sortBy", sortBy)
                        .queryParam("direction", direction)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampResponse.class)
                .value(responses -> {
                    List<BootcampResponse> responsesList = new ArrayList<>(responses);
                    AssertionsForClassTypes.assertThat(responsesList.get(0)).usingRecursiveComparison().isEqualTo(response1);
                    AssertionsForClassTypes.assertThat(responsesList.get(1)).usingRecursiveComparison().isEqualTo(response2);
                });
    }

    @Test
    void testFindAllPaginated_InvalidPaginationParams() {
        List<CapabilityInBootcampResponse> capabilities1 = List.of(new CapabilityInBootcampResponse(1L, "Cap 1", 0, new ArrayList<>()));
        List<CapabilityInBootcampResponse> capabilities2 = List.of(new CapabilityInBootcampResponse(2L, "Cap 2", 0, new ArrayList<>()), new CapabilityInBootcampResponse(3L, "Cap 3", 0, new ArrayList<>()));

        Bootcamp model1 = new Bootcamp(1L, "Bootcamp A", "Description A", new ArrayList<>());
        Bootcamp model2 = new Bootcamp(2L, "Bootcamp B", "Description B", new ArrayList<>());
        BootcampResponse response1 = new BootcampResponse(1L, "Bootcamp A", "Description A", 1, capabilities1);
        BootcampResponse response2 = new BootcampResponse(2L, "Bootcamp B", "Description B", 2, capabilities2);

        when(readBootcampServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION))
                .thenReturn(Flux.just(model1, model2));
        when(bootcampResponseMapper.toBootcampResponse(model1))
                .thenReturn(response1);
        when(bootcampResponseMapper.toBootcampResponse(model2))
                .thenReturn(response2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/bootcamp")
                        .queryParam("page", "-1")
                        .queryParam("size", "DIEZ")
                        .queryParam("sortBy", "robotModel")
                        .queryParam("direction", "bothSides")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampResponse.class)
                .value(responses -> {
                    List<BootcampResponse> responsesList = new ArrayList<>(responses);
                    AssertionsForClassTypes.assertThat(responsesList.get(0)).usingRecursiveComparison().isEqualTo(response1);
                    AssertionsForClassTypes.assertThat(responsesList.get(1)).usingRecursiveComparison().isEqualTo(response2);
                });
    }

    @Test
    void testFindAllPaginated_EmptyResult() {
        when(readBootcampServicePort.findAllPaginated(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION))
                .thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/bootcamp")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BootcampResponse.class)
                .hasSize(0);
    }
}