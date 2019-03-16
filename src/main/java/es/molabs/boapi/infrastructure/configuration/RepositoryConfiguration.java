package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.repository.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.CreatorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CreatorRepository creatorRepository() {
        return new CreatorRepository();
    }

    @Bean
    public CreatorNoteRepository creatorNoteRepository() {
        return new CreatorNoteRepository();
    }
}
