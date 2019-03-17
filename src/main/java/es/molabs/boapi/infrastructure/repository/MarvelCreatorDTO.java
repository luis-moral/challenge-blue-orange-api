package es.molabs.boapi.infrastructure.repository;

public class MarvelCreatorDTO {

    private final int id;
    private final String fullName;
    private final long modified;
    private final int comics;
    private final int series;

    public MarvelCreatorDTO(int id, String fullName, long modified, int comics, int series) {
        this.id = id;
        this.fullName = fullName;
        this.modified = modified;
        this.comics = comics;
        this.series = series;
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
}
