package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Mono;

public class CreatorNoteService {

    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorNoteService(CreatorNoteRepository creatorNoteRepository) {
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public Mono<CreatorNote> findById(int id) {
        return creatorNoteRepository.findById(id);
    }

    public Mono<CreatorNote> addCreatorNote(int creatorId, String text) {
        return creatorNoteRepository.add(creatorId, text);
    }

    public Mono<CreatorNote> editCreatorNote(int id, String text) {
        return
            Mono
                .fromRunnable(() -> creatorNoteRepository.set(id, text))
                .then(creatorNoteRepository.findById(id));
    }

    public void deleteCreatorNote(int id) {
        creatorNoteRepository.deleteById(id);
    }
}
