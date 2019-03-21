package es.molabs.boapi.domain.creatornote;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreatorNoteRepository {

    Flux<CreatorNote> find(FindCreatorNoteQuery query);

    Mono<CreatorNote> findById(int id);

    Mono<CreatorNote> findByCreatorId(int creatorId);

    Mono<CreatorNote> add(int creatorId, String text);

    void set(int id, String text);

    void deleteById(int id);
}
