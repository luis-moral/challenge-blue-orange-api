package es.molabs.boapi.infrastructure.repository.creatornote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE_ID_GENERATOR = "creator_note:id";
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
        return getCreatorNote(key(id));
    }

    @Override
    public Mono<CreatorNote> findByCreatorId(int creatorId) {
        return
            Mono
                .fromCallable(() -> redisClient.get(keyByCreator(creatorId)))
                .flatMap(this::getCreatorNote);
    }

    @Override
    public void add(int creatorId, String text) {
        int id = generateNoteId();

        redisClient.set(keyByCreator(creatorId), key(id));
        redisClient.hmset(key(id), toRedisHash(new CreatorNote(id, creatorId, text)));
    }

    @Override
    public void set(int noteId, String text) {
        redisClient.hset(key(noteId), "text", text);
    }

    @Override
    public void deleteById(int id) {
        CreatorNote note = findById(id).block();

        if (note != null) {
            redisClient.del(keyByCreator(note.getCreatorId()), key(note.getId()));
        }
    }

    private int generateNoteId() {
        int id = 1;
        String lastId = redisClient.get(KEY_CREATOR_NOTE_ID_GENERATOR);

        if (lastId == null || lastId.isEmpty()) {
            redisClient.set(KEY_CREATOR_NOTE_ID_GENERATOR, Integer.toString(id));
        }
        else {
            redisClient.incr(KEY_CREATOR_NOTE_ID_GENERATOR);
            id = Integer.parseInt(redisClient.get(KEY_CREATOR_NOTE_ID_GENERATOR));
        }

        return id;
    }

    private Mono<CreatorNote> getCreatorNote(String key) {
        return
            Mono
                .fromCallable(() -> redisClient.hgetAll(key))
                .filter(fields -> fields.size() > 0)
                .map(this::toCreatorNote);
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
}
