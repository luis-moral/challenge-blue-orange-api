package es.molabs.boapi.domain.creatornote;

import reactor.core.publisher.Mono;

public interface CreatorNoteRepository {

    Mono<CreatorNote> findById(int id);

    Mono<CreatorNote> findByCreatorId(int creatorId);

    void add(int creatorId, String text);

    void set(int id, String text);

    void deleteById(int id);
}
