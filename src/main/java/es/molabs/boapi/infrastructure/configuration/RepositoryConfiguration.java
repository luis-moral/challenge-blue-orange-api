package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.MarvelApiCreatorRepository;
import es.molabs.boapi.infrastructure.repository.RedisCreatorNoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CreatorRepository creatorRepository() {
        return new MarvelApiCreatorRepository();
    }

    @Bean
    public CreatorNoteRepository creatorNoteRepository() {
        return new RedisCreatorNoteRepository();
    }
}
