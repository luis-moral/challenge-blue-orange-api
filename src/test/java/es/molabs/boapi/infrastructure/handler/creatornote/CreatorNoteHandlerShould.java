package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.application.CreatorNoteService;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import org.assertj.core.api.Assertions;
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
import reactor.core.publisher.Mono;

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

        CreatorNoteDTO creatorNoteDTO = new CreatorNoteDTO(1, creatorId, text, null);
        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(creatorId, text);
        CreatorNote creatorNote = new CreatorNote(creatorNoteDTO.getId(), creatorNoteDTO.getCreatorId(), creatorNoteDTO.getText());

        Mockito
            .when(creatorNoteService.addCreatorNote(creatorId, text))
            .thenReturn(Mono.just(creatorNote));

        webTestClient
            .post()
                .uri(builder -> builder.path(creatorNotePath).build(creatorId))
                .syncBody(objectMapper.writeValueAsString(addNoteDto))
            .exchange()
                .expectStatus()
                    .isCreated()
                .expectBody(CreatorNoteDTO.class)
                    .consumeWith(response ->
                        Assertions
                            .assertThat(response.getResponseBody())
                            .isEqualTo(creatorNoteDTO)
                    );

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .addCreatorNote(creatorId, text);
    }

    @Test public void
    edit_a_creator_note() throws JsonProcessingException {
        int id = 1;
        String text = "Some text";

        CreatorNoteDTO creatorNoteDTO = new CreatorNoteDTO(1, 101, text, null);
        EditCreatorNoteDTO editNoteDTO = new EditCreatorNoteDTO(text);
        CreatorNote creatorNote = new CreatorNote(creatorNoteDTO.getId(), creatorNoteDTO.getCreatorId(), creatorNoteDTO.getText());

        Mockito
            .when(creatorNoteService.editCreatorNote(id, text))
            .thenReturn(Mono.just(creatorNote));

        webTestClient
            .put()
                .uri(builder -> builder.path(creatorNotePath).build(id))
                .syncBody(objectMapper.writeValueAsString(editNoteDTO))
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(CreatorNoteDTO.class)
                .consumeWith(response ->
                    Assertions
                        .assertThat(response.getResponseBody())
                        .isEqualTo(creatorNoteDTO)
                );

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .editCreatorNote(id, text);
    }

    @Test public void
    delete_a_creator_note() {
        int id = 1;

        webTestClient
            .delete()
                .uri(builder -> builder.path(creatorNotePath).build(id))
            .exchange()
                .expectStatus()
                    .isOk();

        Mockito
            .verify(creatorNoteService, Mockito.times(1))
            .deleteCreatorNote(id);
    }
}
