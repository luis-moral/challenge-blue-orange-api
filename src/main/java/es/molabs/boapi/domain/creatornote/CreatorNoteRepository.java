package es.molabs.boapi.domain.creatornote;

import reactor.core.publisher.Flux;

import java.util.List;

public interface CreatorNoteRepository {

    Flux<CreatorNote> findByCreatorId(List<Integer> ids);
}
