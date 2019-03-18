package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.test.StepVerifier;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.util.Random;

@RunWith(JUnit4.class)
public class RedisCreatorNoteRepositoryShould {

    private static final int REDIS_PORT;

    static {
        REDIS_PORT = 20000 + new Random().nextInt(25000);
    }

    private RedisServer redisServer;
    private Jedis redisClient;

    private ObjectMapper objectMapper;
    private RedisCreatorNoteRepository creatorNoteRepository;

    @Before
    public void setUp() {
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();

        redisClient = new Jedis("localhost", REDIS_PORT);

        objectMapper = new ObjectMapper();
        creatorNoteRepository = new RedisCreatorNoteRepository(redisClient, objectMapper);
    }

    @After
    public void tearDown() {
        if (redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Test public void
    find_creator_note_by_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        CreatorNote thirdNote = new CreatorNote(3, 103, "Third");

        creatorNoteRepository.add(firstNote);
        creatorNoteRepository.add(secondNote);
        creatorNoteRepository.add(thirdNote);

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .expectNext(firstNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(5))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test public void
    find_creator_note_by_creator_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        CreatorNote thirdNote = new CreatorNote(3, 103, "Third");

        creatorNoteRepository.add(firstNote);
        creatorNoteRepository.add(secondNote);
        creatorNoteRepository.add(thirdNote);

        StepVerifier
            .create(creatorNoteRepository.findByCreatorId(firstNote.getCreatorId()))
            .expectNext(firstNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findByCreatorId(500))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test public void
    add_creator_note_and_index_by_creator_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");

        creatorNoteRepository.add(firstNote);
        creatorNoteRepository.add(secondNote);

        StepVerifier
            .create(creatorNoteRepository.findByCreatorId(firstNote.getCreatorId()))
            .expectNext(firstNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .expectNext(firstNote)
            .verifyComplete();
    }

    @Test public void
    edit_creator_note_by_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");

        creatorNoteRepository.add(firstNote);
        creatorNoteRepository.add(secondNote);

        firstNote.setText("Other Text");

        creatorNoteRepository.set(firstNote);

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .assertNext(note ->
                    Assertions
                        .assertThat(note.getText())
                        .isEqualTo("Other Text")
                )
            .verifyComplete();
    }

    @Test public void
    delete_the_creator_note_by_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");

        creatorNoteRepository.add(firstNote);
        creatorNoteRepository.add(secondNote);

        creatorNoteRepository.deleteById(firstNote.getId());

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .expectNextCount(0)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(secondNote.getId()))
            .expectNext(secondNote)
            .verifyComplete();
    }
}