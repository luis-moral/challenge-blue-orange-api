package es.molabs.boapi.domain.creatornote;

import java.util.Objects;

public class CreatorNote {

    private final int id;
    private final int creatorId;
    private final String text;

    private CreatorNote() {
        this(0, 0, null);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatorNote that = (CreatorNote) o;
        return id == that.id &&
                creatorId == that.creatorId &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorId, text);
    }
}
