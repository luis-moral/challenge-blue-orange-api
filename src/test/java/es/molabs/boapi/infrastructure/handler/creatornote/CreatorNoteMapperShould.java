package es.molabs.boapi.infrastructure.handler.creatornote;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreatorNoteMapperShould {

    private CreatorNoteMapper mapper;

    @Before
    public void setUp() {
        mapper = new CreatorNoteMapper();
    }

    @Test public void
    map_creator_note_to_creator_note_dto() {
        String fullName = "Some Name";
        CreatorNote creatorNote = new CreatorNote(1, 101, "Some text");
        CreatorNoteDTO creatorNoteDTOWithFullName = new CreatorNoteDTO(1, creatorNote.getCreatorId(), creatorNote.getText(), null);
        CreatorNoteDTO creatorNoteDTOWithoutFullName = new CreatorNoteDTO(1, creatorNote.getCreatorId(), creatorNote.getText(), fullName);

        Assertions
            .assertThat(mapper.toCreatorNoteDTO(creatorNote, fullName))
            .isEqualTo(creatorNoteDTOWithFullName);

        Assertions
            .assertThat(mapper.toCreatorNoteDTO(creatorNote))
            .isEqualTo(creatorNoteDTOWithoutFullName);
    }
}