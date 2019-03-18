package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.creator.CreatorHandler;
import es.molabs.boapi.infrastructure.handler.health.HealthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Value("${endpoint.creator.path}")
    private String creatorsPath;

    @Value("${endpoint.health.path}")
    private String healthPath;

    @Bean
    public RouterFunction<ServerResponse> routes(
        CreatorHandler creatorHandler,
        HealthHandler healthHandler
    ) {
        return
            RouterFunctions
                .route(
                    RequestPredicates.GET(creatorsPath),
                    creatorHandler::getCreators
                )
                .andRoute(
                    RequestPredicates.GET(healthPath),
                    healthHandler::health
                );
    }
}
