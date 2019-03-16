package es.molabs.boapi.application;

import es.molabs.boapi.domain.Creator;
import es.molabs.boapi.infrastructure.repository.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.CreatorRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class CreatorServiceShould {

    private CreatorRepository creatorRepository;
    private CreatorNoteRepository creatorNoteRepository;

    private CreatorService creatorService;

    @Test public void
    retrieve_creators_and_add_their_notes() {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        Creator fistCreator = creator(1);
        Creator secondCreator = creator(2);
        List<Creator> creators = Arrays.asList(fistCreator, secondCreator);

        creatorService.findCreators(query);

        Mockito
            .verify(creatorRepository, Mockito.times(1))
            .find(query);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .find(creators);
    }

    private Creator creator(int id) {
        return new Creator(String.valueOf(id), "", 123, 4, 5, "");
    }
}