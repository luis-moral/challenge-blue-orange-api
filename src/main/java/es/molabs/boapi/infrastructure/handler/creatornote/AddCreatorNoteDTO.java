package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddCreatorNoteDTO {

    private final int creatorId;
    private final String text;

    @JsonCreator
    public AddCreatorNoteDTO(
        @JsonProperty("creatorId") int creatorId,
        @JsonProperty("note") String text)
    {
        this.creatorId = creatorId;
        this.text = text;
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
        AddCreatorNoteDTO that = (AddCreatorNoteDTO) o;
        return creatorId == that.creatorId &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creatorId, text);
    }
}
