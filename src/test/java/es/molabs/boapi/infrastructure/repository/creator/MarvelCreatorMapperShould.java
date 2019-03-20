package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.Creator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MarvelCreatorMapperShould {

    private MarvelCreatorMapper mapper;

    @Before
    public void setUp() {
        mapper = new MarvelCreatorMapper();
    }

    @Test public void
    map_marvel_creator_dto_to_creator() {
        MarvelCreatorDTO creatorDTO =
            new MarvelCreatorDTO(
                1,
                "Some Name",
                "Some Date",
                new MarvelCreatorDTO.ItemsDTO(4),
                new MarvelCreatorDTO.ItemsDTO(3)
            );

        Creator creator = mapper.toCreator(creatorDTO);

        Assertions
            .assertThat(creator.getId())
            .isEqualTo(creatorDTO.getId());

        Assertions
            .assertThat(creator.getFullName())
            .isEqualTo(creatorDTO.getFullName());

        Assertions
            .assertThat(creator.getModified())
            .isEqualTo(creatorDTO.getModified());

        Assertions
            .assertThat(creator.getComics())
            .isEqualTo(4);

        Assertions
            .assertThat(creator.getSeries())
            .isEqualTo(3);
    }

}