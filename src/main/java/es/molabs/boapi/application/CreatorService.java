package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Flux;

public class CreatorService {

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
                .doOnNext(creator ->
                    creatorNoteRepository
                        .findByCreatorId(creator.getId())
                        .doOnNext(note -> creator.setNote(note))
                );
    }
}
