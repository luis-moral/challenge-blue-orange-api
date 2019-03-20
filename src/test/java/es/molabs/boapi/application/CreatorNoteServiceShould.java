package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    add_creator_notes() {
        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(1, "Some text");

        creatorNoteService.addCreatorNote(addNoteDto);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .add(addNoteDto);
    }
    
    @Test public void 
    edit_creator_notes() {
        int id = 1;
        EditCreatorNoteDTO editNoteDto = new EditCreatorNoteDTO("Some text");

        creatorNoteService.editCreatorNote(id, editNoteDto);

        Mockito
            .verify(creatorNoteRepository, Mockito.times(1))
            .set(id, editNoteDto);
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