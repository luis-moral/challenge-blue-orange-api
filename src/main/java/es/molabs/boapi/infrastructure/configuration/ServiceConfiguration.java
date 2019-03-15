package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.application.CreatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public CreatorService creatorService() {
        return new CreatorService();
    }
}
