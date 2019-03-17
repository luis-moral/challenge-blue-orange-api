package es.molabs.boapi.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    private static final String KEY_CREATOR_NOTE_BY_CREATOR = "creator_note_by_creator_";

    private final ObjectMapper objectMapper;
    private final Jedis redisClient;

    public RedisCreatorNoteRepository(String host, int port, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        redisClient = new Jedis(host, port);
    }

    @Override
    public List<CreatorNote> findByCreatorId(List<Integer> creatorIds) {
        return
            creatorIds
                .stream()
                .map(creatorId -> redisClient.get(KEY_CREATOR_NOTE_BY_CREATOR + creatorId))
                .map(key -> objectMapper.convertValue(redisClient.hgetAll(key), CreatorNote.class))
                .collect(Collectors.toList());
    }
}