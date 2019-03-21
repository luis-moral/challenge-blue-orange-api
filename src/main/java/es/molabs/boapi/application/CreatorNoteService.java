package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;

public class CreatorNoteService {

    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorNoteService(CreatorNoteRepository creatorNoteRepository) {
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public void findById(int id) {
        throw new UnsupportedOperationException();
    }

    public void addCreatorNote(AddCreatorNoteDTO dto) {
        creatorNoteRepository.add(dto.getCreatorId(), dto.getText());
    }

    public void editCreatorNote(int id, EditCreatorNoteDTO dto) {
        creatorNoteRepository.set(id, dto.getText());
    }

    public void deleteCreatorNote(int id) {
        creatorNoteRepository.deleteById(id);
    }
}
