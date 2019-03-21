package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Optional;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE_ID_GENERATOR = "creator_note:id";
    private static final String KEY_CREATOR_NOTE = "creator_note_";
    private static final String KEY_CREATOR_NOTE_BY_CREATOR = "creator_note_by_creator_";

    private final ObjectMapper objectMapper;
    private final JedisPool redisPool;

    public RedisCreatorNoteRepository(JedisPool redisPool, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.redisPool = redisPool;
    }

    @Override
    public Mono<CreatorNote> findById(int id) {
        return
            Mono
                .fromCallable(() -> getCreatorNote(key(id)));
    }

    @Override
    public Mono<CreatorNote> findByCreatorId(int creatorId) {
        return
            Mono
                .fromCallable(() -> redis(client -> client.get(keyByCreator(creatorId))))
                .map(this::getCreatorNote);
    }

    @Override
    public Mono<CreatorNote> add(int creatorId, String text) {
        return
            Mono
                .fromCallable(() -> generateNoteId())
                .doOnNext(id -> {
                    redis(client -> client.set(keyByCreator(creatorId), key(id)));
                    redis(client -> client.hmset(key(id), toRedisHash(new CreatorNote(id, creatorId, text))));
                })
                .flatMap(id -> findById(id));
    }

    @Override
    public void set(int noteId, String text) {
        redis(client -> client.hset(key(noteId), "text", text));
    }

    @Override
    public void deleteById(int id) {
        CreatorNote note = getCreatorNote(key(id));

        if (note != null) {
            redis(client -> client.del(keyByCreator(note.getCreatorId()), key(note.getId())));
        }
    }

    private int generateNoteId() {
        int id = 1;
        String lastId = redis(client -> client.get(KEY_CREATOR_NOTE_ID_GENERATOR));

        if (lastId == null || lastId.isEmpty()) {
            redis(client -> client.set(KEY_CREATOR_NOTE_ID_GENERATOR, "1"));
        }
        else {
            redis(client -> client.incr(KEY_CREATOR_NOTE_ID_GENERATOR));
            id = Integer.parseInt(redis(client -> client.get(KEY_CREATOR_NOTE_ID_GENERATOR)));
        }

        return id;
    }

    private CreatorNote getCreatorNote(String key) {
        return
            Optional
                .ofNullable(redis(client-> client.hgetAll(key)))
                .filter(fields -> fields.size() > 0)
                .map(this::toCreatorNote)
                .orElse(null);

    }

    private CreatorNote toCreatorNote(Map<String, String> redisHash) {
        return objectMapper.convertValue(redisHash, CreatorNote.class);
    }

    private<T> Map<String, String> toRedisHash(T note) {
        return objectMapper.convertValue(note, new TypeReference<Map<String, String>>() {});
    }

    private String key(int id) {
        return KEY_CREATOR_NOTE + id;
    }

    private String keyByCreator(int creatorId) {
        return KEY_CREATOR_NOTE_BY_CREATOR + creatorId;
    }

    private<T> T redis(RedisAction<T> action) {
        try (Jedis client = redisPool.getResource()) {
            return action.doAction(client);
        }
    }

    @FunctionalInterface
    private interface RedisAction<T> {
        T doAction(Jedis client);
    }
}
