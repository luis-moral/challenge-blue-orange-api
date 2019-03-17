package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Flux;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE_BY_CREATOR = "creator_note_by_creator_";

    private final ObjectMapper objectMapper;
    private final Jedis redisClient;

    public RedisCreatorNoteRepository(String host, int port, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        redisClient = new Jedis(host, port);
    }

    @Override
    public Flux<CreatorNote> findByCreatorId(List<Integer> creatorIds) {
        return
            Flux
                .fromIterable(creatorIds)
                .map(creatorId -> redisClient.get(KEY_CREATOR_NOTE_BY_CREATOR + creatorId))
                .map(key -> objectMapper.convertValue(redisClient.hgetAll(key), CreatorNote.class));
    }
}
