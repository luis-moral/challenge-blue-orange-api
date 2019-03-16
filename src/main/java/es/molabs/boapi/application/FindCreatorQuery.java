package es.molabs.boapi.application;

public class FindCreatorQuery {

    public final static FindCreatorQuery EMPTY = new FindCreatorQuery(null, null, null, null, null, null, null);

    private final String id;
    private final String fullName;
    private final String modified;
    private final String comics;
    private final String series;
    private final String notes;
    private final SortQuery sortQuery;

    public FindCreatorQuery(
        String id,
        String fullName,
        String modified,
        String comics,
        String series,
        String notes,
        SortQuery sortQuery
    ) {
        this.id = id;
        this.fullName = fullName;
        this.modified = modified;
        this.comics = comics;
        this.series = series;
        this.notes = notes;
        this.sortQuery = sortQuery;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getModified() {
        return modified;
    }

    public String getComics() {
        return comics;
    }

    public String getSeries() {
        return series;
    }

    public String getNotes() {
        return notes;
    }

    public SortQuery getSortQuery() {
        return sortQuery;
    }
}
