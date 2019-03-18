package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import reactor.core.publisher.Flux;

public class MarvelApiCreatorRepository implements CreatorRepository {

    private final MarvelApiClient apiClient;
    private final MarvelCreatorMapper mapper;

    public MarvelApiCreatorRepository(MarvelApiClient apiClient, MarvelCreatorMapper mapper) {
        this.apiClient = apiClient;
        this.mapper = mapper;
    }

    @Override
    public Flux<Creator> find(FindCreatorQuery query) {
        return
            apiClient
                .get(query)
                .map(mapper::toCreator);
    }
}
