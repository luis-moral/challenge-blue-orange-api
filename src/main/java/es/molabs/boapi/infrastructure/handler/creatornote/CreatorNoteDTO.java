package es.molabs.boapi.infrastructure.handler.creatornote;

public class CreatorNoteDTO {

    private final int id;
    private final int creatorId;
    private final String text;

    public CreatorNoteDTO(int id, int creatorId, String text) {
        this.id = id;
        this.creatorId = creatorId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getText() {
        return text;
    }
}
