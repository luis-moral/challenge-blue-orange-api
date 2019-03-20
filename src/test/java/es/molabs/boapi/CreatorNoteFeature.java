package es.molabs.boapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.CreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CreatorNoteFeature {

    @Value("${endpoint.creator-note.path}")
    private String creatorNotePath;
    @Value("${marvel.api.base-url}")
    private String marvelBaseUrl;
    @Value("${marvel.api.key}")
    private String marvelApiKey;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private CreatorNoteRepository creatorNoteRepository;

    private WireMockServer marvelApiMock;

    @Before
    public void setUp() throws URISyntaxException {
        URI marvelBaseUri = new URI(marvelBaseUrl);
        int port =
            marvelBaseUri.getPort() != -1
                ? marvelBaseUri.getPort()
                : marvelBaseUri.getScheme().startsWith("https") ? 443 : 80;

        marvelApiMock = new WireMockServer(port);
        marvelApiMock.start();
    }

    @After
    public void tearDown() {
        if (marvelApiMock.isRunning()) {
            marvelApiMock.stop();
        }
    }

    @Test public void
    clients_can_manage_creator_notes() throws JsonProcessingException {
        int noteId = 1;
        int creatorId = 201;
        String text = "Some text";
        String updatedText = "Some other text";

        assertCreateNote(creatorId, text, noteId);
        StepVerifier
            .create(creatorNoteRepository.findById(noteId))
            .expectNext(new CreatorNote(noteId, creatorId, text))
            .verifyComplete();

        assertEditNote(noteId, creatorId, updatedText);
        StepVerifier
            .create(creatorNoteRepository.findById(noteId))
            .expectNext(new CreatorNote(noteId, creatorId, updatedText))
            .verifyComplete();

        assertDeleteNote(noteId);
        StepVerifier
            .create(creatorNoteRepository.findById(noteId))
            .expectNextCount(0)
            .verifyComplete();
    }

    private void assertCreateNote(int creatorId, String text, int expectedNoteId) throws JsonProcessingException {
        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(creatorId, text);
        CreatorNoteDTO noteDto = new CreatorNoteDTO(expectedNoteId, creatorId, text);

        webTestClient
            .post()
                .uri(builder -> builder.path(creatorNotePath).build(creatorId))
                .syncBody(objectMapper.writeValueAsString(addNoteDto))
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(new ParameterizedTypeReference<CreatorNoteDTO>() {})
                    .consumeWith(response ->
                        Assertions
                            .assertThat(response.getResponseBody())
                            .isEqualTo(noteDto)
                     );
    }

    private void assertEditNote(int noteId, int creatorId, String text) throws JsonProcessingException {
        EditCreatorNoteDTO editDTO = new EditCreatorNoteDTO(text);
        CreatorNoteDTO noteNoteDto = new CreatorNoteDTO(noteId, creatorId, text);

        webTestClient
            .put()
                .uri(builder -> builder.path(creatorNotePath).build(noteId))
                .syncBody(objectMapper.writeValueAsString(editDTO))
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(new ParameterizedTypeReference<CreatorNoteDTO>() {})
                    .consumeWith(response ->
                        Assertions
                            .assertThat(response.getResponseBody())
                            .isEqualTo(noteNoteDto)
                    );
    }

    private void assertDeleteNote(int noteId) throws JsonProcessingException {
        webTestClient
            .delete()
                .uri(builder -> builder.path(creatorNotePath).build(noteId))
            .exchange()
                .expectStatus()
                   .isOk();
    }
}
