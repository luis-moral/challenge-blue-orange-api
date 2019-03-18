package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreatorMapperShould {

    private CreatorMapper mapper;

    @Before
    public void setUp() {
        mapper = new CreatorMapper();
    }

    @Test
    public void map_creator_without_note_to_creator_dto() {
        Creator creator =
            new Creator(
                1,
                "some_name",
                "123",
                5,
                5
            );

        assertDto(mapper.toCreatorDTO(creator), creator);
    }

    @Test
    public void map_creator_with_note_to_creator_dto() {
        Creator creator =
            new Creator(
                1,
                "some_name",
                "123",
                5,
                5,
                new CreatorNote(2, 1, "Super")
            );

        assertDto(mapper.toCreatorDTO(creator), creator);
    }

    private void assertDto(CreatorDTO dto, Creator creator) {
        Assertions
            .assertThat(dto.getId())
            .isEqualTo(creator.getId());

        Assertions
            .assertThat(dto.getFullName())
            .isEqualTo(creator.getFullName());

        Assertions
            .assertThat(dto.getModified())
            .isEqualTo(creator.getModified());

        Assertions
            .assertThat(dto.getComics())
            .isEqualTo(creator.getComics());

        Assertions
            .assertThat(dto.getSeries())
            .isEqualTo(creator.getSeries());

        if (creator.getNote() != null) {
            Assertions
                .assertThat(dto.getNote())
                .isEqualTo(creator.getNote().getText());
        } else {
            Assertions
                .assertThat(dto.getNote())
                .isNull();
        }
    }
}