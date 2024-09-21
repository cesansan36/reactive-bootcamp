package com.rutaaprendizajewebflux.bootcamp.configuration.routerconfiguration;

import com.rutaaprendizajewebflux.bootcamp.application.dto.request.SaveBootcampRequest;
import com.rutaaprendizajewebflux.bootcamp.application.dto.response.BootcampResponse;
import com.rutaaprendizajewebflux.bootcamp.application.handler.IBootcampHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
public class ServiceRouterConfiguration {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(IBootcampHandler bootcampHandler) {
        return route()
                .POST("/bootcamp", bootcampHandler::save, saveBootcampConsumer())
                .GET("/bootcamp", bootcampHandler::findAllPaginated, findAllPaginatedConsumer())
                .build();
    }

    private Consumer<Builder> saveBootcampConsumer () {
        return builder -> builder
                .operationId("saveBootcamp")
                .summary("⭐ Save Bootcamp")
                .requestBody(requestBodyBuilder().implementation(SaveBootcampRequest.class))
                .response(responseBuilder().responseCode("200").description("OK").implementation(BootcampResponse.class));
    }

    private Consumer<Builder> findAllPaginatedConsumer () {
        return builder -> builder
                .operationId("findAllPaginated")
                .summary("⭐ Find All Paginated")
                .tag("⭐ Bootcamp")
                .parameter(parameterBuilder().name("page").description("Page number").in(ParameterIn.QUERY).required(true).example("0"))
                .parameter(parameterBuilder().name("size").description("Page size").in(ParameterIn.QUERY).required(true).example("3"))
                .parameter(parameterBuilder().name("sortBy").description("Sort by").in(ParameterIn.QUERY).required(true).example("name"))
                .parameter(parameterBuilder().name("direction").description("Sort direction").in(ParameterIn.QUERY).required(true).example("ASC"))
                .response(responseBuilder().responseCode("200").description("OK").implementationArray(BootcampResponse.class));
    }
}
