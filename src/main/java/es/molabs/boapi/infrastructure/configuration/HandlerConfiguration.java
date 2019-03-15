package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.CreatorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public CreatorHandler creatorHandler() {
        return new CreatorHandler();
    }
}
