package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.repository.creator.MarvelApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class ClientConfiguration {

    @Value("${marvel.api.host}")
    private String marvelApiHost;
    @Value("${marvel.api.port}")
    private int marvelApiPort;
    @Value("${marvel.api.key}")
    private String marvelApiKey;

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public MarvelApiClient marvelApiClient() {
        return new MarvelApiClient(marvelApiHost, marvelApiPort, marvelApiKey);
    }

    @Bean
    public Jedis redisClient() {
        return new Jedis(redisHost, redisPort);
    }
}
