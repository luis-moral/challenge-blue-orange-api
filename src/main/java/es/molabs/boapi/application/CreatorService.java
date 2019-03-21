package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Flux;

public class CreatorService {

    private static final CreatorNote EMPTY_NOTE = new CreatorNote(-99, -99, null);

    private final CreatorRepository creatorRepository;
    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorService(CreatorRepository creatorRepository, CreatorNoteRepository creatorNoteRepository) {
        this.creatorRepository = creatorRepository;
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public Flux<Creator> findCreators(FindCreatorQuery query) {
        return
            creatorRepository
                .find(query)
                .flatMap(creator ->
                    creatorNoteRepository
                        .findByCreatorId(creator.getId())
                        .defaultIfEmpty(EMPTY_NOTE)
                        .map(note -> setNote(creator, note))
                );

    }

    private Creator setNote(Creator creator, CreatorNote note) {
        if (note != EMPTY_NOTE) {
            creator.setNote(note);
        }

        return creator;
    }
}
