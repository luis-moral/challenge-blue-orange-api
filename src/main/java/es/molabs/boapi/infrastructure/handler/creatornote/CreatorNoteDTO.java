package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatorNoteDTO {

    private final int id;
    private final int creatorId;
    private final String text;

    @JsonCreator
    public CreatorNoteDTO(
        @JsonProperty("id") int id,
        @JsonProperty("creatorId") int creatorId,
        @JsonProperty("text") String text)
    {
        this.id = id;
        this.creatorId = creatorId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatorNoteDTO that = (CreatorNoteDTO) o;
        return id == that.id &&
            creatorId == that.creatorId &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorId, text);
    }
}
