package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class CreatorNoteServiceShould {

    @Mock
    private CreatorNoteRepository creatorNoteRepository;

    private CreatorNoteService creatorNoteService;

    @Before
    public void setUp() {
        creatorNoteService = new CreatorNoteService(creatorNoteRepository);
    }

    @Test public void
    find_notes_by_id() {
        int id = 1;
        CreatorNote creatorNote = Mockito.mock(CreatorNote.class);

        Mockito
            .when(creatorNoteRepository.findById(id))
            .thenReturn(Mono.just(creatorNote));

        StepVerifier
            .create(creatorNoteService.findById(id))
            .expectNext(creatorNote)
            .verifyComplete();
    }

    @Test public void 
    add_creator_notes() {
        int id = 1;
        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(101, "Some text");
        CreatorNote creatorNote = new CreatorNote(id, addNoteDto.getCreatorId(), addNoteDto.getText());

        Mockito
            .when(creatorNoteRepository.add(addNoteDto.getCreatorId(), addNoteDto.getText()))
            .thenReturn(Mono.just(creatorNote));

        StepVerifier
            .create(creatorNoteService.addCreatorNote(addNoteDto.getCreatorId(), addNoteDto.getText()))
            .expectNext(creatorNote)
            .verifyComplete();

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .add(addNoteDto.getCreatorId(), addNoteDto.getText());
    }
    
    @Test public void 
    edit_creator_notes() {
        int id = 1;
        EditCreatorNoteDTO editNoteDto = new EditCreatorNoteDTO("Some text");
        CreatorNote creatorNote = new CreatorNote(id, 101, editNoteDto.getText());

        Mockito
            .when(creatorNoteRepository.findById(id))
            .thenReturn(Mono.just(creatorNote));

        StepVerifier
            .create(creatorNoteService.editCreatorNote(id, editNoteDto.getText()))
            .expectNext(creatorNote)
            .verifyComplete();

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .set(id, editNoteDto.getText());

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .findById(id);
    }
    
    @Test public void 
    delete_creator_notes() {
        int id = 1;

        creatorNoteService.deleteCreatorNote(id);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .deleteById(id);
    }
}