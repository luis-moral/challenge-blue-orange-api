package es.molabs.boapi.infrastructure.handler.creatornote;

public class AddCreatorNoteDTO {

    private final int creatorId;
    private final String text;

    public AddCreatorNoteDTO(int creatorId, String text) {
        this.creatorId = creatorId;
        this.text = text;
    }
}
