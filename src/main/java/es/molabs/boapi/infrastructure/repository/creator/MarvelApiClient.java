package es.molabs.boapi.infrastructure.repository.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
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

    private final String apiKey;
    private final WebClient webClient;
    private final FindCreatorQueryMapper queryMapper;
    private final ObjectMapper objectMapper;

    public MarvelApiClient(
        String baseUrl,
        String apiKey,
        WebClient webClient,
        FindCreatorQueryMapper queryMapper,
        ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey;
        this.webClient = webClient.mutate().baseUrl(baseUrl).build();
        this.queryMapper = queryMapper;
        this.objectMapper = objectMapper;
    }

    public Flux<MarvelCreatorDTO> get(FindCreatorQuery query) {
        return
            webClient
                .get()
                    .uri(builder -> buildUri(builder, "/v1/public/creators", query))
                .exchange()
                    .flatMapMany(this::toDTO);
    }

    private URI buildUri(UriBuilder builder, String path, FindCreatorQuery query) {
        MultiValueMap<String, String> queryParams = queryMapper.toMap(query);
        queryParams.set(PARAMETER_API_KEY, apiKey);

        return
            builder
                .path(path)
                .queryParams(queryParams)
                .build();
    }

    private Flux<MarvelCreatorDTO> toDTO(ClientResponse clientResponse) {
        return
            clientResponse
                .bodyToMono(String.class)
                .map(body -> {
                    try {
                        return objectMapper.readValue(body, MarvelGetCreatorResponseDTO.class);
                    } catch (IOException IOe) {
                        throw new RuntimeException(IOe);
                    }
                })
                .flatMapIterable(response -> response.getMarvelCreators());
    }

    public Mono<MarvelCreatorDTO> get(int id) {
        throw new UnsupportedOperationException();
    }
}
