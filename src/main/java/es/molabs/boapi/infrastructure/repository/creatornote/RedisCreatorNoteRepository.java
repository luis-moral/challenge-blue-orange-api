package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE_BY_CREATOR = "creator_note_by_creator_";

    private final ObjectMapper objectMapper;
    private final Jedis redisClient;

    public RedisCreatorNoteRepository(Jedis redisClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redisClient = redisClient;
    }

    @Override
    public Mono<CreatorNote> findByCreatorId(int creatorId) {
        return
            Mono
                .just(creatorId)
                .map(id -> redisClient.get(KEY_CREATOR_NOTE_BY_CREATOR + id))
                .map(key -> objectMapper.convertValue(redisClient.hgetAll(key), CreatorNote.class));
    }
}
