package es.molabs.boapi.infrastructure.handler.creatornote;

import es.molabs.boapi.domain.creatornote.CreatorNote;

public class CreatorNoteMapper {

    public CreatorNoteDTO toCreatorNoteDTO(CreatorNote creatorNote) {
        return toCreatorNoteDTO(creatorNote, null);
    }

    public CreatorNoteDTO toCreatorNoteDTO(CreatorNote creatorNote, String creatorFullName) {
        return
            new CreatorNoteDTO(
                creatorNote.getId(),
                creatorNote.getCreatorId(),
                creatorNote.getText(),
                creatorFullName
            );
    }
}
