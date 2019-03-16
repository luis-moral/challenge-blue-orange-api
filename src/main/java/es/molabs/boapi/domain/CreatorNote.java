package es.molabs.boapi.domain;

public class CreatorNote {

    private final int id;
    private final int creatorId;
    private final String text;

    public CreatorNote(int id, int creatorId, String text) {
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
