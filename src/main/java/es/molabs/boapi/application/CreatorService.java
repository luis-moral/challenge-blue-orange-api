package es.molabs.boapi.application;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.CreatorRepository;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creatornote.CreatorNote;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
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
                .map(this::toCreatorIdTuple)
                .map(this::toMapByCreatorId)
                .flatMapIterable(this::addNotesToCreators);
    }

    private Tuple2<List<Creator>, List<Integer>> toCreatorIdTuple(List<Creator> creators) {
        return
            Tuples
                .of(
                    creators,
                    creators
                        .stream()
                        .map(creator -> creator.getId()).collect(Collectors.toList()));
    }

    private Tuple2<List<Creator>, Map<Integer, CreatorNote>> toMapByCreatorId(Tuple2<List<Creator>, List<Integer>> creatorIdTuple) {
        return
            creatorIdTuple
                .mapT2(
                    creatorIds ->
                        creatorNoteRepository
                            .findByCreatorId(creatorIds)
                            .toStream()
                            .collect(Collectors.toConcurrentMap(note -> note.getCreatorId(), note -> note))
                );
    }

    private List<Creator> addNotesToCreators(Tuple2<List<Creator>, Map<Integer, CreatorNote>> creatorNoteTuple) {
        return
            creatorNoteTuple
                .getT1()
                .stream()
                .map(creator -> {
                    creator.setNote(creatorNoteTuple.getT2().get(creator.getId()));
                    return creator;
                })
                .collect(Collectors.toList());
    }
}
