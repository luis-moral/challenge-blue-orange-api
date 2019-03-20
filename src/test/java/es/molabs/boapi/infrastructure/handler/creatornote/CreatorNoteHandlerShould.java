package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.application.CreatorNoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreatorNoteHandlerShould {

    @Value("${endpoint.creator-note.path}")
    private String creatorNotePath;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreatorNoteService creatorNoteService;

    @Test public void
    add_a_creator_note_to_a_creator() throws JsonProcessingException {
        int creatorId = 101;
        String text = "Some text";

        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(creatorId, text);

        webTestClient
            .post()
                .uri(builder -> builder.path(creatorNotePath).build(creatorId))
                .syncBody(objectMapper.writeValueAsString(addNoteDto))
            .exchange()
                .expectStatus()
                    .isCreated();

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .addCreatorNote(addNoteDto);
    }

    @Test public void
    edit_a_creator_note() throws JsonProcessingException {
        int noteId = 1;
        String text = "Some text";

        EditCreatorNoteDTO editNoteDTO = new EditCreatorNoteDTO(text);

        webTestClient
            .put()
                .uri(builder -> builder.path(creatorNotePath).build(noteId))
                .syncBody(objectMapper.writeValueAsString(editNoteDTO))
            .exchange()
                .expectStatus()
                    .isOk();

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .editCreatorNote(editNoteDTO);
    }

    @Test public void
    delete_a_creator_note() {
        int noteId = 1;

        webTestClient
            .delete()
                .uri(builder -> builder.path(creatorNotePath).build(noteId))
            .exchange()
                .expectStatus()
                    .isOk();

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .deleteCreatorNote(noteId);
    }
}
