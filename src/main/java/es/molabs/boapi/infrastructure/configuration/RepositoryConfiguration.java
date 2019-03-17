package es.molabs.boapi.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.MarvelApiCreatorRepository;
import es.molabs.boapi.infrastructure.repository.RedisCreatorNoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public CreatorRepository creatorRepository() {
        return new MarvelApiCreatorRepository();
    }

    @Bean
    public CreatorNoteRepository creatorNoteRepository(ObjectMapper objectMapper) {
        return new RedisCreatorNoteRepository(redisHost, redisPort, objectMapper);
    }
}
