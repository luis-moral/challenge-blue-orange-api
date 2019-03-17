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
    public void map_creators_to_creator_dtos() {
        Creator creator =
            new Creator(
                1,
                "some_name",
                System.currentTimeMillis() + 12500,
                5,
                5,
                new CreatorNote(2, 1, "Super")
            );

        CreatorDTO dto = mapper.toCreatorDTO(creator);

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

        Assertions
            .assertThat(dto.getNote())
            .isEqualTo(creator.getNote().getText());
    }
}