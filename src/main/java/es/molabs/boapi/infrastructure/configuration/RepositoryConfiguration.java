package es.molabs.boapi.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.creator.MarvelApiClient;
import es.molabs.boapi.infrastructure.repository.creator.MarvelApiCreatorRepository;
import es.molabs.boapi.infrastructure.repository.creator.MarvelCreatorMapper;
import es.molabs.boapi.infrastructure.repository.creatornote.RedisCreatorNoteRepository;
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
    public MarvelApiClient apiClient() {
        return new MarvelApiClient();
    }

    @Bean
    public CreatorRepository creatorRepository(MarvelApiClient apiClient, MarvelCreatorMapper mapper) {
        return new MarvelApiCreatorRepository(apiClient, mapper);
    }

    @Bean
    public CreatorNoteRepository creatorNoteRepository(ObjectMapper objectMapper) {
        return new RedisCreatorNoteRepository(redisHost, redisPort, objectMapper);
    }
}
