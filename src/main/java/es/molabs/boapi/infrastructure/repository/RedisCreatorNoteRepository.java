package es.molabs.boapi.infrastructure.repository;

import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;

import java.util.List;

public class RedisCreatorNoteRepository implements CreatorNoteRepository {

    @Override
    public List<CreatorNote> find(List<Integer> ids) {
        return null;
    }
}
