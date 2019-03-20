package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.application.CreatorNoteService;
import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public CreatorService creatorService(CreatorRepository creatorRepository, CreatorNoteRepository creatorNoteRepository) {
        return new CreatorService(creatorRepository, creatorNoteRepository);
    }

    @Bean
    public CreatorNoteService creatorNoteService(CreatorNoteRepository creatorNoteRepository) {
        return new CreatorNoteService(creatorNoteRepository);
    }
}
