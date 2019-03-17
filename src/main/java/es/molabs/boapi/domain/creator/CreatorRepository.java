package es.molabs.boapi.domain.creator;

import es.molabs.boapi.application.FindCreatorQuery;
import reactor.core.publisher.Flux;

public interface CreatorRepository {

    Flux<Creator> find(FindCreatorQuery query);
}
