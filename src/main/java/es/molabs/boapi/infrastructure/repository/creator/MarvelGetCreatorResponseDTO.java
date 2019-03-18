package es.molabs.boapi.infrastructure.repository.creator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelGetCreatorResponseDTO {

    private final DataDTO data;

    @JsonCreator
    public MarvelGetCreatorResponseDTO(@JsonProperty("data") DataDTO data) {
        this.data = data;
    }

    public List<MarvelCreatorDTO> getMarvelCreators() {
        return data.getResults();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataDTO {

        private final List<MarvelCreatorDTO> results;

        @JsonCreator
        public DataDTO(@JsonProperty("results") List<MarvelCreatorDTO> results) {
            this.results = results;
        }

        public List<MarvelCreatorDTO> getResults() {
            return results;
        }
    }
}
