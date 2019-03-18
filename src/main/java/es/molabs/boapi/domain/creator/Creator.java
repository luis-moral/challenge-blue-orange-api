package es.molabs.boapi.domain.creator;

import es.molabs.boapi.domain.creatornote.CreatorNote;

public class Creator {

    private int id;
    private String fullName;
    private String modified;
    private int comics;
    private int series;
    private CreatorNote note;

    public Creator(int id, String fullName, String modified, int comics, int series) {
        this(id, fullName, modified, comics, series, null);
    }

    public Creator(int id, String fullName, String modified, int comics, int series, CreatorNote note) {
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

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
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
