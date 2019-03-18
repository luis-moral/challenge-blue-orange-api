package es.molabs.boapi.infrastructure.repository.creator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelCreatorDTO {

    private final int id;
    private final String fullName;
    private final String modified;
    private final int comics;
    private final int series;

    @JsonCreator
    public MarvelCreatorDTO(
        @JsonProperty("id") int id,
        @JsonProperty("fullName") String fullName,
        @JsonProperty("modified") String modified,
        @JsonProperty("comics") ItemsDTO comics,
        @JsonProperty("series") ItemsDTO series
    ) {
        this.id = id;
        this.fullName = fullName;
        this.modified = modified;
        this.comics = comics.getAvailable();
        this.series = series.getAvailable();
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemsDTO {

        private final int available;

        @JsonCreator
        public ItemsDTO(@JsonProperty("available") int available) {
            this.available = available;
        }

        public int getAvailable() {
            return available;
        }
    }
}
