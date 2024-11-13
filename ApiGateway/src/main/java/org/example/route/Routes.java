package org.example.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> ProductServiceRoutes() {
        return GatewayRouterFunctions.route("product-service")
                .route(RequestPredicates.path("api/v1/product") , HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> OrderServiceRoutes() {
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("api/v1/order") , HandlerFunctions.http("http://localhost:8084"))
                .build();
    }
}
