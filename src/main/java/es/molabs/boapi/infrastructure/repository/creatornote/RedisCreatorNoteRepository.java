package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE = "creator_note_";
    private static final String KEY_CREATOR_NOTE_BY_CREATOR = "creator_note_by_creator_";

    private final ObjectMapper objectMapper;
    private final Jedis redisClient;

    public RedisCreatorNoteRepository(Jedis redisClient, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redisClient = redisClient;
    }

    @Override
    public Mono<CreatorNote> findById(int id) {
        return get(key(id));
    }

    @Override
    public Mono<CreatorNote> findByCreatorId(int creatorId) {
        return
            Mono
                .fromCallable(() -> redisClient.get(keyByCreator(creatorId)))
                .flatMap(this::get);
    }

    @Override
    public void add(CreatorNote note) {
        redisClient.set(keyByCreator(note.getCreatorId()), key(note.getId()));
        redisClient.hmset(key(note.getId()), toRedisHash(note));
    }

    @Override
    public void set(CreatorNote note) {
        add(note);
    }

    @Override
    public void deleteById(int id) {
        CreatorNote note = findById(id).block();

        if (note != null) {
            redisClient.del(keyByCreator(note.getCreatorId()), key(note.getId()));
        }
    }

    private String key(int id) {
        return KEY_CREATOR_NOTE + id;
    }

    private String keyByCreator(int creatorId) {
        return KEY_CREATOR_NOTE_BY_CREATOR + creatorId;
    }

    private Mono<CreatorNote> get(String key) {
        return
            Mono
                .fromCallable(() -> redisClient.hgetAll(key))
                .filter(fields -> fields.size() > 0)
                .map(this::toCreatorNote);
    }

    private CreatorNote toCreatorNote(Map<String, String> redisHash) {
        return objectMapper.convertValue(redisHash, CreatorNote.class);
    }

    private Map<String, String> toRedisHash(CreatorNote note) {
        return objectMapper.convertValue(note, new TypeReference<Map<String, String>>() {});
    }
}
