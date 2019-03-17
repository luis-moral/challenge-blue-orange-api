package es.molabs.boapi.domain.creator;

import reactor.core.publisher.Flux;

public interface CreatorRepository {

    Flux<Creator> find(FindCreatorQuery query);
}
