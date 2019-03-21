package es.molabs.boapi.domain.creatornote;

public class FindCreatorNoteQuery {

    public static final FindCreatorNoteQuery EMPTY = new FindCreatorNoteQuery(null, null);

    private final Integer creatorId;
    private final String text;

    public FindCreatorNoteQuery(Integer creatorId) {
        this(creatorId, null);
    }

    public FindCreatorNoteQuery(String text) {
        this(null, text);
    }

    private FindCreatorNoteQuery(Integer creatorId, String text) {
        this.creatorId = creatorId;
        this.text = text;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public String getText() {
        return text;
    }
}
