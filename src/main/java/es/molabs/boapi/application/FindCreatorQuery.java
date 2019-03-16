package es.molabs.boapi.application;

import java.util.Optional;

public class FindCreatorQuery {

    private final Optional<String> id;
    private final Optional<String> fullName;
    private final Optional<Long> modified;
    private final Optional<Integer> comics;
    private final Optional<Integer> series;
    private final Optional<String> notes;
    private final Optional<SortQuery> sortQuery;

    public FindCreatorQuery(
        String id,
        String fullName,
        Long modified,
        Integer comics,
        Integer series,
        String notes,
        SortQuery sortQuery
    ) {
        this.id = Optional.ofNullable(id);
        this.fullName = Optional.ofNullable(fullName);
        this.modified = Optional.ofNullable(modified);
        this.comics = Optional.ofNullable(comics);
        this.series = Optional.ofNullable(series);
        this.notes = Optional.ofNullable(notes);
        this.sortQuery = Optional.ofNullable(sortQuery);
    }

    public Optional<String> getId() {
        return id;
    }

    public Optional<String> getFullName() {
        return fullName;
    }

    public Optional<Long> getModified() {
        return modified;
    }

    public Optional<Integer> getComics() {
        return comics;
    }

    public Optional<Integer> getSeries() {
        return series;
    }

    public Optional<String> getNotes() {
        return notes;
    }

    public Optional<SortQuery> getSortQuery() {
        return sortQuery;
    }
}
