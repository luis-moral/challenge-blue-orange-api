package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class MarvelApiClient {

    private final String apiKey;
    private final WebClient webClient;
    private final FindCreatorQueryMapper queryMapper;

    public MarvelApiClient(String baseUrl, String apiKey, WebClient webClient, FindCreatorQueryMapper queryMapper) {
        this.apiKey = apiKey;
        this.webClient = webClient.mutate().baseUrl(baseUrl).build();
        this.queryMapper = queryMapper;
    }

    public Flux<MarvelCreatorDTO> get(FindCreatorQuery query) {
        return
            webClient
                .get()
                    .uri(builder -> builder.path("/v1/public/creators").queryParams(queryMapper.toMap(query)).build())
                .exchange()
                    .flatMapMany(this::toDTO);
    }

    private Flux<MarvelCreatorDTO> toDTO(ClientResponse clientResponse) {
        throw new UnsupportedOperationException();
    }
}
