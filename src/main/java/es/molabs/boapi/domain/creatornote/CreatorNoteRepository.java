package es.molabs.boapi.domain.creatornote;

import reactor.core.publisher.Mono;

public interface CreatorNoteRepository {

    Mono<CreatorNote> findById(int id);

    Mono<CreatorNote> findByCreatorId(int creatorId);

    void add(CreatorNote note);

    void set(CreatorNote note);

    void deleteById(int id);
}
