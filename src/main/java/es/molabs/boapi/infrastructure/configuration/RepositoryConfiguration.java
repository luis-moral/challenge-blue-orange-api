package es.molabs.boapi.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.creator.MarvelApiClient;
import es.molabs.boapi.infrastructure.repository.creator.MarvelApiCreatorRepository;
import es.molabs.boapi.infrastructure.repository.creator.MarvelCreatorMapper;
import es.molabs.boapi.infrastructure.repository.creatornote.RedisCreatorNoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CreatorRepository creatorRepository(MarvelApiClient apiClient, MarvelCreatorMapper mapper) {
        return new MarvelApiCreatorRepository(apiClient, mapper);
    }

    @Bean
    public CreatorNoteRepository creatorNoteRepository(Jedis redisClient, ObjectMapper objectMapper) {
        return new RedisCreatorNoteRepository(redisClient, objectMapper);
    }
}
