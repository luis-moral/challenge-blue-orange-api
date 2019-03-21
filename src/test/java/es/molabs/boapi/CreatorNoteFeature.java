package es.molabs.boapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creatornote.AddCreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.CreatorNoteDTO;
import es.molabs.boapi.infrastructure.handler.creatornote.EditCreatorNoteDTO;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class CreatorNoteFeature {

    @Value("${endpoint.creators-notes.path}")
    private String creatorsNotesPath;
    @Value("${endpoint.creator-note.path}")
    private String creatorNotePath;
    @Value("${marvel.api.base-url}")
    private String marvelBaseUrl;
    @Value("${marvel.api.key}")
    private String marvelApiKey;
    @Value("${redis.port}")
    private int redisPort;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private CreatorNoteRepository creatorNoteRepository;

    private WireMockServer marvelApiMock;
    private RedisServer redisServer;

    @Before
    public void setUp() throws URISyntaxException {
        URI marvelBaseUri = new URI(marvelBaseUrl);
        int port =
            marvelBaseUri.getPort() != -1
                ? marvelBaseUri.getPort()
                : marvelBaseUri.getScheme().startsWith("https") ? 443 : 80;

        marvelApiMock = new WireMockServer(port);
        marvelApiMock.start();

        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @After
    public void tearDown() {
        if (marvelApiMock.isRunning()) {
            marvelApiMock.stop();
        }

        if (redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Test public void
    clients_can_get_a_note_by_id() throws IOException {
        CreatorNoteDTO firstNoteDTO = new CreatorNoteDTO(1, 101, "Some Text", "Some Name");
        CreatorNoteDTO secondNoteDTO = new CreatorNoteDTO(2, 102, "Other Text", "Other Name");

        creatorNoteRepository.add(firstNoteDTO.getCreatorId(), firstNoteDTO.getText()).block();
        creatorNoteRepository.add(secondNoteDTO.getCreatorId(), secondNoteDTO.getText()).block();

        stubMarvelApi(firstNoteDTO.getCreatorId());

        assertGetNote(firstNoteDTO);
    }

    @Test public void
    clients_can_list_current_notes() throws IOException {
        String fistFullName = "Some Name";
        String secondFullName = "Other Name";

        CreatorNoteDTO firstNoteDTO = new CreatorNoteDTO(1, 101, "Some Text", fistFullName);
        CreatorNoteDTO secondNoteDTO = new CreatorNoteDTO(2, 102, "Other Text", secondFullName);

        creatorNoteRepository.add(firstNoteDTO.getCreatorId(), firstNoteDTO.getText()).block();
        creatorNoteRepository.add(secondNoteDTO.getCreatorId(), secondNoteDTO.getText()).block();

        stubMarvelApi(firstNoteDTO.getCreatorId());
        stubMarvelApi(secondNoteDTO.getCreatorId());

        assertListNotes(null, null, firstNoteDTO, secondNoteDTO);
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

    private void stubMarvelApi(int creatorId) throws IOException {
        marvelApiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/v1/public/creators/" + creatorId))
                    .withQueryParam("apikey", WireMock.equalTo(marvelApiKey))
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                                .withBody(
                                    readFile("/creator/get_single_creator_by_id.json")
                                        .replace("\"id\": 1,", "\"id\": " + creatorId + ",")
                                )
                    )
            );
    }

    private void assertGetNote(CreatorNoteDTO expectedNote) {
        webTestClient
            .get()
                .uri(builder ->
                    builder.path(creatorNotePath).build(expectedNote.getId())
                )
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(CreatorNoteDTO.class)
                .consumeWith(response ->
                    Assertions
                        .assertThat(response.getResponseBody())
                        .isEqualTo(expectedNote)
            );
    }

    private void assertListNotes(Integer creatorId, String text, CreatorNoteDTO...expectedNotes) {
        webTestClient
            .get()
                .uri(builder ->
                    builder.path(creatorsNotesPath).queryParams(toQueryParams(creatorId, text)).build()
                )
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(new ParameterizedTypeReference<List<CreatorNoteDTO>>() {})
                .consumeWith(response ->
                    Assertions
                        .assertThat(response.getResponseBody())
                        .contains(expectedNotes)
            );
    }
    private void assertCreateNote(int creatorId, String text, int expectedNoteId) throws JsonProcessingException {
        AddCreatorNoteDTO addNoteDto = new AddCreatorNoteDTO(creatorId, text);
        CreatorNoteDTO noteDto = new CreatorNoteDTO(expectedNoteId, creatorId, text, null);

        webTestClient
            .post()
                .uri(builder -> builder.path(creatorNotePath).build(creatorId))
                .syncBody(objectMapper.writeValueAsString(addNoteDto))
            .exchange()
                .expectStatus()
                    .isCreated()
                .expectBody(new ParameterizedTypeReference<CreatorNoteDTO>() {})
                    .consumeWith(response ->
                        Assertions
                            .assertThat(response.getResponseBody())
                            .isEqualTo(noteDto)
                     );
    }

    private void assertEditNote(int noteId, int creatorId, String text) throws JsonProcessingException {
        EditCreatorNoteDTO editDTO = new EditCreatorNoteDTO(text);
        CreatorNoteDTO noteNoteDto = new CreatorNoteDTO(noteId, creatorId, text, null);

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

    private void assertDeleteNote(int noteId) {
        webTestClient
            .delete()
                .uri(builder -> builder.path(creatorNotePath).build(noteId))
            .exchange()
                .expectStatus()
                   .isOk();
    }

    private MultiValueMap toQueryParams(Integer creatorId, String text) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        if (creatorId != null) {
            queryParams.set("creatorId", Integer.toString(creatorId));
        }

        if (text != null) {
            queryParams.set("text", text);
        }

        return queryParams;
    }

    private String readFile(String resource) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8);
    }
}
