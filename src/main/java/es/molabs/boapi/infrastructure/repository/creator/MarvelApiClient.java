package es.molabs.boapi.infrastructure.repository.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

public class MarvelApiClient {

    private static final String PARAMETER_API_KEY = "apikey";
    private static final String PARAMETER_HASH = "hash";
    private static final String PARAMETER_TIMESTAMP = "ts";

    private final String publicApiKey;
    private final String privateApiKey;
    private final WebClient webClient;
    private final FindCreatorQueryMapper queryMapper;
    private final ObjectMapper objectMapper;
    private final TimestampGenerator timestampGenerator;

    public MarvelApiClient(
        String baseUrl,
        String publicApiKey,
        String privateApiKey,
        WebClient webClient,
        FindCreatorQueryMapper queryMapper,
        ObjectMapper objectMapper,
        TimestampGenerator timestampGenerator
    ) {
        this.publicApiKey = publicApiKey;
        this.privateApiKey = privateApiKey;
        this.webClient = webClient.mutate().baseUrl(baseUrl).build();
        this.queryMapper = queryMapper;
        this.objectMapper = objectMapper;
        this.timestampGenerator = timestampGenerator;
    }

    public Flux<MarvelCreatorDTO> get(FindCreatorQuery query) {
        return
            webClient
                .get()
                    .uri(builder -> buildUri(builder, "/v1/public/creators", query))
                .exchange()
                    .flatMapMany(this::toDTO);
    }

    public Mono<MarvelCreatorDTO> get(int id) {
        return
            webClient
                .get()
                    .uri(builder ->
                        builder
                            .path("/v1/public/creators/{id}")
                            .queryParams(addRequiredParams(new LinkedMultiValueMap<>()))
                            .build(id)
                    )
                .exchange()
                    .flatMap(response -> toDTO(response).next());
    }

    private URI buildUri(UriBuilder builder, String path, FindCreatorQuery query) {
        return
            builder
                .path(path)
                .queryParams(
                    addRequiredParams(
                        queryMapper.toMarvelApiQuery(query))
                    )
                .build();
    }

    private MultiValueMap<String, String> addRequiredParams(MultiValueMap<String, String> queryParams) {
        String timestamp =  Long.toString(timestampGenerator.nextId());

        queryParams.set(PARAMETER_API_KEY, publicApiKey);
        queryParams.set(PARAMETER_HASH, getHash(timestamp));
        queryParams.set(PARAMETER_TIMESTAMP ,timestamp);

        return queryParams;
    }

    private String getHash(String timestamp) {
        return DigestUtils.md5DigestAsHex((timestamp + privateApiKey + publicApiKey).getBytes());
    }

    private Flux<MarvelCreatorDTO> toDTO(ClientResponse response) {
        return
            response
                .bodyToMono(String.class)
                .map(body -> {
                    try {
                        return objectMapper.readValue(body, MarvelGetCreatorResponseDTO.class);
                    } catch (IOException IOe) {
                        throw new RuntimeException(IOe);
                    }
                })
                .filter(dto -> dto.getMarvelCreators() != null)
                .flatMapIterable(dto -> dto.getMarvelCreators());
    }
}
