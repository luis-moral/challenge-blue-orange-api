package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.CreatorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfiguration {

    @Value("${endpoint.creator.path}")
    private String creatorsPath;

    @Bean
    public RouterFunction<ServerResponse> routes(CreatorHandler creatorHandler) {
        return
            RouterFunctions
                .route(
                    RequestPredicates.GET(creatorsPath),
                    creatorHandler::getCreators
                );
    }
}
