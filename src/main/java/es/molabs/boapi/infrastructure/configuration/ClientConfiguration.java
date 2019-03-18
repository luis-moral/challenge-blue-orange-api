package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.repository.creator.MarvelApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.Jedis;

@Configuration
public class ClientConfiguration {

    @Value("${marvel.api.base-url}")
    private String marvelbaseUrl;
    @Value("${marvel.api.key}")
    private String marvelApiKey;

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public MarvelApiClient marvelApiClient(WebClient webClient) {
        return new MarvelApiClient(marvelbaseUrl, marvelApiKey, webClient);
    }

    @Bean
    public Jedis redisClient() {
        return new Jedis(redisHost, redisPort);
    }
}