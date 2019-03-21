package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    find_creators_by_id() {
        int id = 101;
        Creator creator = creator(id);

        Mockito
            .when(creatorRepository.findById(id))
            .thenReturn(Mono.just(creator));

        StepVerifier
            .create(creatorService.findById(id))
            .expectNext(creator)
            .verifyComplete();
    }

    @Test public void
    retrieve_creators_and_add_their_notes() {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        Creator firstCreator = creator(1);
        Creator firstCreatorWithNote = creator(1);
        Creator secondCreator = creator(2);

        CreatorNote firstNote = new CreatorNote(10, firstCreator.getId(), "First");
        firstCreatorWithNote.setNote(firstNote);

        Mockito
            .when(creatorRepository.find(query))
            .thenReturn(Flux.just(firstCreator, secondCreator));

        Mockito
            .when(creatorNoteRepository.findByCreatorId(firstCreator.getId()))
            .thenReturn(Mono.just(firstNote));

        Mockito
            .when(creatorNoteRepository.findByCreatorId(secondCreator.getId()))
            .thenReturn(Mono.empty());

        StepVerifier
            .create(creatorService.find(query))
            .expectNext(firstCreatorWithNote, secondCreator)
            .verifyComplete();

        Mockito
            .verify(creatorRepository, Mockito.times(1))
            .find(query);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .findByCreatorId(firstCreator.getId());

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .findByCreatorId(secondCreator.getId());
    }

    private Creator creator(int id) {
        return new Creator(id, "", "123", 4, 5);
    }
}