package es.molabs.boapi.application;

import es.molabs.boapi.domain.Creator;
import es.molabs.boapi.domain.CreatorNote;
import es.molabs.boapi.infrastructure.repository.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.repository.CreatorRepository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final CreatorNoteRepository creatorNoteRepository;

    public CreatorService(CreatorRepository creatorRepository, CreatorNoteRepository creatorNoteRepository) {
        this.creatorRepository = creatorRepository;
        this.creatorNoteRepository = creatorNoteRepository;
    }

    public List<Creator> findCreators(FindCreatorQuery query) {
        return
            Mono
                .fromCallable(() -> creatorRepository.find(query))
                .map(creators ->
                    Tuples
                        .of(
                            creators,
                            creators
                                .stream()
                                .map(creator -> creator.getId()).collect(Collectors.toList()))
                    )
                .map(tuple -> tuple.mapT2(values -> toMapByCreatorId(creatorNoteRepository.find(values))))
                .map(tuple ->
                        tuple
                            .getT1()
                            .stream()
                            .map(creator -> {
                                creator.setNote(tuple.getT2().get(creator.getId()));
                                return creator;
                            })
                            .collect(Collectors.toList())
                    )
                .block();
    }

    private Map<Integer, CreatorNote> toMapByCreatorId(List<CreatorNote> creatorNotes) {
        Map<Integer, CreatorNote> creatorNoteMap = new ConcurrentHashMap<>();

        if (creatorNotes != null && !creatorNotes.isEmpty()) {
            creatorNotes
                .stream()
                .forEach(note -> creatorNoteMap.put(note.getCreatorId(), note));
        }

        return creatorNoteMap;
    }
}
