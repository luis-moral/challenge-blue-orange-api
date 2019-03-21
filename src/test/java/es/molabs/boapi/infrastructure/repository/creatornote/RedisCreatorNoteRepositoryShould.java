package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.FindCreatorNoteQuery;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.test.StepVerifier;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.util.Arrays;
import java.util.Random;

@RunWith(JUnit4.class)
public class RedisCreatorNoteRepositoryShould {

    private static final int REDIS_PORT;

    static {
        REDIS_PORT = 20000 + new Random().nextInt(25000);
    }

    private RedisServer redisServer;
    private JedisPool redisClientPool;

    private ObjectMapper objectMapper;
    private RedisCreatorNoteRepository creatorNoteRepository;

    @Before
    public void setUp() {
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();

        redisClientPool = new JedisPool(new JedisPoolConfig(), "localhost", REDIS_PORT);

        objectMapper = new ObjectMapper();
        creatorNoteRepository = new RedisCreatorNoteRepository(redisClientPool, objectMapper);
    }

    @After
    public void tearDown() {
        if (redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Test public void
    find_creator_note_by_query() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "First Again");
        CreatorNote thirdNote = new CreatorNote(3, 103, "Third");
        addCreatorNotes(firstNote, secondNote, thirdNote);

        FindCreatorNoteQuery firstQuery = new FindCreatorNoteQuery(103);
        FindCreatorNoteQuery secondQuery = new FindCreatorNoteQuery("First");

        StepVerifier
            .create(creatorNoteRepository.find(firstQuery))
            .expectNext(thirdNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.find(secondQuery))
            .expectNext(firstNote, secondNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.find(FindCreatorNoteQuery.EMPTY))
            .expectNext(firstNote, secondNote, thirdNote)
            .verifyComplete();
    }

    @Test public void
    find_creator_note_by_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        CreatorNote thirdNote = new CreatorNote(3, 103, "Third");
        addCreatorNotes(firstNote, secondNote, thirdNote);

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
        addCreatorNotes(firstNote, secondNote, thirdNote);

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

        addCreatorNotes(firstNote, secondNote);

        StepVerifier
            .create(creatorNoteRepository.findByCreatorId(secondNote.getCreatorId()))
            .expectNext(secondNote)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(secondNote.getId()))
            .expectNext(secondNote)
            .verifyComplete();
    }

    @Test public void
    edit_current_note_if_already_exists_when_adding() {
        CreatorNote firstAdd = creatorNoteRepository.add(101, "Some Text").block();
        CreatorNote secondAdd = creatorNoteRepository.add(101, "Other Text").block();

        Assertions
            .assertThat(secondAdd.getId())
            .isEqualTo(firstAdd.getId());
    }

    @Test public void
    edit_creator_note_by_id() {
        String otherText = "Other text";
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        CreatorNote firstNoteUpdated = new CreatorNote(firstNote.getId(), firstNote.getCreatorId(), otherText);
        addCreatorNotes(firstNote, secondNote);

        StepVerifier
            .create(creatorNoteRepository.set(firstNote.getId(), otherText))
            .expectNext(firstNoteUpdated)
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .expectNext(firstNoteUpdated)
            .verifyComplete();
    }

    @Test public void
    fail_if_edit_a_note_that_does_no_exists() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        String otherText = "Other text";

        StepVerifier
            .create(creatorNoteRepository.set(firstNote.getId(), otherText))
            .expectError();
    }

    @Test public void
    delete_the_creator_note_by_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        addCreatorNotes(firstNote, secondNote);

        creatorNoteRepository.deleteById(firstNote.getId());

        StepVerifier
            .create(creatorNoteRepository.findById(firstNote.getId()))
            .verifyComplete();

        StepVerifier
            .create(creatorNoteRepository.findById(secondNote.getId()))
            .expectNext(secondNote)
            .verifyComplete();
    }

    private void addCreatorNotes(CreatorNote...creatorNotes) {
        Arrays
            .stream(creatorNotes)
            .forEach(creatorNote ->
                creatorNoteRepository.add(creatorNote.getCreatorId(), creatorNote.getText()).block()
            );
    }
}