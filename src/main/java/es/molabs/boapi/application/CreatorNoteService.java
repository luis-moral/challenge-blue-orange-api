package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import reactor.core.publisher.Mono;

public class CreatorNoteService {

    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorNoteService(CreatorNoteRepository creatorNoteRepository) {
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public Mono<CreatorNote> findById(int id) {
        return creatorNoteRepository.findById(id);
    }

    public Mono<CreatorNote> addCreatorNote(AddCreatorNoteDTO dto) {
        return creatorNoteRepository.add(dto.getCreatorId(), dto.getText());
    }

    public Mono<CreatorNote> editCreatorNote(int id, EditCreatorNoteDTO dto) {
        return
            Mono
                .fromRunnable(() -> creatorNoteRepository.set(id, dto.getText()))
                .then(creatorNoteRepository.findById(id));
    }

    public void deleteCreatorNote(int id) {
        creatorNoteRepository.deleteById(id);
    }
}
