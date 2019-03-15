package es.molabs.boapi.infrastructure.handler;

public class CreatorDTO {

    private final String id;
    private final String fullName;
    private final long modified;
    private final int comics;
    private final int series;
    private final String notes;

    public CreatorDTO(String id, String fullName, long modified, int comics, int series, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.modified = modified;
        this.comics = comics;
        this.series = series;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public long getModified() {
        return modified;
    }

    public int getComics() {
        return comics;
    }

    public int getSeries() {
        return series;
    }

    public String getNotes() {
        return notes;
    }
}
