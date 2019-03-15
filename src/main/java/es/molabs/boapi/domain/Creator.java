package es.molabs.boapi.domain;

public class Creator {

    private String id;
    private String fullName;
    private long modified;
    private int comics;
    private int series;
    private String notes;

    public Creator(String id, String fullName, long modified, int comics, int series, String notes) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public int getComics() {
        return comics;
    }

    public void setComics(int comics) {
        this.comics = comics;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
