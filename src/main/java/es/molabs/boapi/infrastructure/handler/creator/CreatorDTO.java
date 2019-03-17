package es.molabs.boapi.infrastructure.handler.creator;

public class CreatorDTO {

    private final int id;
    private final String fullName;
    private final long modified;
    private final int comics;
    private final int series;
    private final String note;

    public CreatorDTO(int id, String fullName, long modified, int comics, int series, String note) {
        this.id = id;
        this.fullName = fullName;
        this.modified = modified;
        this.comics = comics;
        this.series = series;
        this.note = note;
    }

    public int getId() {
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

    public String getNote() {
        return note;
    }
}
