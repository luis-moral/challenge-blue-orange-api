package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.FindCreatorQuery;

import java.util.List;

public class MarvelApiClient {

    private final String host;
    private final int port;
    private final String apiKey;

    public MarvelApiClient(String host, int port, String apiKey) {
        this.host = host;
        this.port = port;
        this.apiKey = apiKey;
    }

    public List<MarvelCreatorDTO> get(FindCreatorQuery query) {
        throw new UnsupportedOperationException();
    }
}
