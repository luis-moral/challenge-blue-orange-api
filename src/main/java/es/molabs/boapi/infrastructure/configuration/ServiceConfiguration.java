package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.infrastructure.repository.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.CreatorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public CreatorService creatorService(CreatorRepository creatorRepository, CreatorNoteRepository creatorNoteRepository) {
        return new CreatorService(creatorRepository, creatorNoteRepository);
    }
}
