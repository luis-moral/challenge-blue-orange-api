package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.domain.creatornote.FindCreatorNoteQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreatorNoteService {

    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorNoteService(CreatorNoteRepository creatorNoteRepository) {
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public Flux<CreatorNote> find(FindCreatorNoteQuery query) {
        return creatorNoteRepository.find(query);
    }

    public Mono<CreatorNote> findById(int id) {
        return creatorNoteRepository.findById(id);
    }

    public Mono<CreatorNote> addCreatorNote(int creatorId, String text) {
        return creatorNoteRepository.add(creatorId, text);
    }

    public Mono<CreatorNote> editCreatorNote(int id, String text) {
        return creatorNoteRepository.set(id, text);
    }

    public void deleteCreatorNote(int id) {
        creatorNoteRepository.deleteById(id);
    }
}
