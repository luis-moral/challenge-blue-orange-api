package es.molabs.boapi.domain.creatornote;

import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import reactor.core.publisher.Mono;

public interface CreatorNoteRepository {

    Mono<CreatorNote> findById(int id);

    Mono<CreatorNote> findByCreatorId(int creatorId);

    void add(AddCreatorNoteDTO dto);

    void set(int id, EditCreatorNoteDTO dt);

    void deleteById(int id);
}
