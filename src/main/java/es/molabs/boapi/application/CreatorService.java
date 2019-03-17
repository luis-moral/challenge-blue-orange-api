package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

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

    public Flux<Creator> findCreators(FindCreatorQuery query) {
        return
            creatorRepository.find(query)
                .collectList()
                .map(creators ->
                    Tuples
                        .of(
                            creators,
                            creators
                                .stream()
                                .map(creator -> creator.getId()).collect(Collectors.toList()))
                )
                .map(tuple -> tuple.mapT2(values -> toMapByCreatorId(creatorNoteRepository.findByCreatorId(values))))
                .flatMapIterable(tuple ->
                    tuple
                        .getT1()
                        .stream()
                        .map(creator -> {
                            creator.setNote(tuple.getT2().get(creator.getId()));
                            return creator;
                        })
                        .collect(Collectors.toList())
                );

    }

    private Map<Integer, CreatorNote> toMapByCreatorId(List<CreatorNote> creatorNotes) {
        Map<Integer, CreatorNote> creatorNoteMap = null;

        if (creatorNotes != null) {
            creatorNoteMap =
                creatorNotes
                    .stream()
                    .collect(Collectors.toConcurrentMap(note -> note.getCreatorId(), note -> note));
        }

        return creatorNoteMap != null ? creatorNoteMap : new ConcurrentHashMap<>();
    }
}
