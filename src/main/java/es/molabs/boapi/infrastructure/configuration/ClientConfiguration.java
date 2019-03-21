package es.molabs.boapi.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import es.molabs.boapi.infrastructure.repository.creator.MarvelApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ClientConfiguration {

    @Value("${marvel.api.base-url}")
    private String marvelBaseUrl;
    @Value("${marvel.api.public-key}")
    private String marvelPublicApiKey;
    @Value("${marvel.api.private-key}")
    private String marvelPrivateApiKey;

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public MarvelApiClient marvelApiClient(
        WebClient webClient,
        FindCreatorQueryMapper queryMapper,
        ObjectMapper objectMapper
    ) {
        return new MarvelApiClient(marvelBaseUrl, marvelPublicApiKey, marvelPrivateApiKey, webClient, queryMapper, objectMapper);
    }

    @Bean
    public JedisPool redisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        return new JedisPool(jedisPoolConfig, redisHost, redisPort);
    }
}
