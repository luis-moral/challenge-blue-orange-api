package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.domain.Creator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

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
                "some_id",
                "some_name",
                System.currentTimeMillis() + 12500,
                5,
                5,
                "Super"
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
            .assertThat(dto.getNotes())
            .isEqualTo(creator.getNotes());
    }
}