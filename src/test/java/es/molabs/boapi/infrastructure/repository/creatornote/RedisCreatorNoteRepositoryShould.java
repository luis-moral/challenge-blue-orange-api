package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.test.StepVerifier;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.util.Map;
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
    find_creator_notes_by_creator_id() {
        CreatorNote firstNote = new CreatorNote(1, 101, "First");
        CreatorNote secondNote = new CreatorNote(2, 102, "Second");
        CreatorNote thirdNote = new CreatorNote(3, 103, "Third");

        addNote(firstNote);
        addNote(secondNote);
        addNote(thirdNote);

        StepVerifier
            .create(creatorNoteRepository.findByCreatorId(firstNote.getCreatorId()))
            .expectNext(firstNote)
            .verifyComplete();
    }

    private void addNote(CreatorNote note) {
        redisClient.set("creator_note_by_creator_" + note.getCreatorId(), "creator_note_" + note.getId());
        redisClient.hmset("creator_note_" + note.getId(), objectMapper.convertValue(note, new TypeReference<Map<String, String>>() {}));
    }
}