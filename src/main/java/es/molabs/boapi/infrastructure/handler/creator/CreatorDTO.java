package es.molabs.boapi.infrastructure.handler.creator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatorDTO {

    private final int id;
    private final String fullName;
    private final String modified;
    private final int comics;
    private final int series;
    private final String note;

    @JsonCreator
    public CreatorDTO(
        @JsonProperty("id") int id,
        @JsonProperty("fullName") String fullName,
        @JsonProperty("modified") String modified,
        @JsonProperty("comics") int comics,
        @JsonProperty("series") int series,
        @JsonProperty("note") String note
    ) {
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

    public String getModified() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatorDTO that = (CreatorDTO) o;
        return id == that.id &&
                comics == that.comics &&
                series == that.series &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(modified, that.modified) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, modified, comics, series, note);
    }
}
