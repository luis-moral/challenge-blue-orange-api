package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class MarvelApiClient {

    private final String apiKey;
    private final WebClient webClient;

    public MarvelApiClient(String baseUrl, String apiKey, WebClient webClient) {
        this.apiKey = apiKey;
        this.webClient = webClient.mutate().baseUrl(baseUrl).build();
    }

    public List<MarvelCreatorDTO> get(FindCreatorQuery query) {
        return null;
    }
}
