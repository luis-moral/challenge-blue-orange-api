package es.molabs.boapi.infrastructure.repository.creatornote;

public class CreatorNoteNoteFoundException extends RuntimeException {

    public CreatorNoteNoteFoundException(int noteId) {
        super("Creator note not found, id=" + noteId);
    }
}
