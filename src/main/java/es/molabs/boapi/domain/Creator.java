package es.molabs.boapi.domain;

public class Creator {

    private int id;
    private String fullName;
    private long modified;
    private int comics;
    private int series;
    private CreatorNote note;

    public Creator(int id, String fullName, long modified, int comics, int series) {
        this(id, fullName, modified, comics, series, null);
    }

    public Creator(int id, String fullName, long modified, int comics, int series, CreatorNote note) {
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

    public void setId(int id) {
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

    public CreatorNote getNote() {
        return note;
    }

    public void setNote(CreatorNote note) {
        this.note = note;
    }
}
