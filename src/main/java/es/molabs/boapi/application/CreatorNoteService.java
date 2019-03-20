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

    public void addCreatorNote(AddCreatorNoteDTO addCreatorNoteDTO) {
        throw new UnsupportedOperationException();
    }

    public void editCreatorNote(EditCreatorNoteDTO editCreatorNoteDTO) {
        throw new UnsupportedOperationException();
    }

    public void deleteCreatorNote(int id) {
        throw new UnsupportedOperationException();
    }
}
