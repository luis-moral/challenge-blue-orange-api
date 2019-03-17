package es.molabs.boapi.domain.creatornote;

import reactor.core.publisher.Mono;

public interface CreatorNoteRepository {

    Mono<CreatorNote> findByCreatorId(int creatorId);
}
