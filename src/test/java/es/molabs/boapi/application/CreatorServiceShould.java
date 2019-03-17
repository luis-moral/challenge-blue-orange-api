package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.domain.creator.CreatorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class CreatorServiceShould {

    @Mock
    private CreatorRepository creatorRepository;
    @Mock
    private CreatorNoteRepository creatorNoteRepository;

    private CreatorService creatorService;

    @Before
    public void setUp() {
        creatorService = new CreatorService(creatorRepository, creatorNoteRepository);
    }

    @Test public void
    retrieve_creators_and_add_their_notes() {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        Creator firstCreator = creator(1);
        Creator secondCreator = creator(2);
        List<Creator> repositoryCreators = Arrays.asList(firstCreator, secondCreator);
        List<Integer> repositoryCreatorsIds = repositoryCreators.stream().map(creator -> creator.getId()).collect(Collectors.toList());

        CreatorNote firstNote = new CreatorNote(10, firstCreator.getId(), "First");
        CreatorNote secondNote = new CreatorNote(20, secondCreator.getId(), "Second");
        List<CreatorNote> repositoryNotes = Arrays.asList(firstNote, secondNote);

        Mockito
            .when(creatorRepository.find(query))
            .thenReturn(repositoryCreators);

        Mockito
            .when(creatorNoteRepository.findByCreatorId(repositoryCreatorsIds))
            .thenReturn(repositoryNotes);

        StepVerifier
            .create(creatorService.findCreators(query))
            .expectNext(firstCreator, secondCreator)
            .verifyComplete();

        Mockito
            .verify(creatorRepository, Mockito.times(1))
            .find(query);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .findByCreatorId(repositoryCreatorsIds);
    }

    private Creator creator(int id) {
        return new Creator(id, "", 123, 4, 5);
    }
}