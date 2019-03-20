package es.molabs.boapi.application;

import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CreatorNoteServiceShould {

    @Mock
    private CreatorNoteRepository creatorNoteRepository;

    private CreatorNoteService creatorService;

    @Before
    public void setUp() {
        creatorService = new CreatorNoteService(creatorNoteRepository);
    }

    @Test public void 
    add_creator_notes() {
    }
    
    @Test public void 
    edit_creator_notes() {
    }
    
    @Test public void 
    delete_creator_notes() {
    }
}