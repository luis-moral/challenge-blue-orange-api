package es.molabs.boapi.domain.creator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreatorRepository {

    Flux<Creator> find(FindCreatorQuery query);

    Mono<Creator> findById(int id);
}
