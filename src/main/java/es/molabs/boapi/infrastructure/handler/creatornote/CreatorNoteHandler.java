package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.molabs.boapi.application.CreatorNoteService;
import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.domain.creatornote.FindCreatorNoteQuery;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatorNoteHandler {

    private final CreatorNoteService creatorNoteService;
    private final CreatorNoteMapper creatorNoteMapper;
    private final CreatorService creatorService;

    public CreatorNoteHandler(CreatorNoteService creatorNoteService, CreatorNoteMapper creatorNoteMapper, CreatorService creatorService) {
        this.creatorNoteService = creatorNoteService;
        this.creatorNoteMapper = creatorNoteMapper;
        this.creatorService = creatorService;
    }

    public Mono<ServerResponse> getCreatorNotesByQuery(ServerRequest serverRequest) {
        return
            creatorNoteService
                .find(buildQuery(serverRequest))
                .flatMap(creatorNote ->
                    creatorService
                        .findById(creatorNote.getCreatorId())
                        .map(creator -> creatorNoteMapper.toCreatorNoteDTO(creatorNote, creator.getFullName()))
                )
                .collectList()
                .flatMap(dtoList ->
                    ServerResponse
                        .ok()
                        .body(Flux.fromIterable(dtoList), CreatorNoteDTO.class)
                );
    }

    public Mono<ServerResponse> getCreatorNote(ServerRequest serverRequest) {
        return
            creatorNoteService
                .findById(Integer.parseInt(serverRequest.pathVariable("id")))
                .flatMap(creatorNote ->
                    creatorService
                        .findById(creatorNote.getCreatorId())
                        .map(creator -> creatorNoteMapper.toCreatorNoteDTO(creatorNote, creator.getFullName()))
                )
                .flatMap(dto ->
                    ServerResponse
                        .ok()
                        .body(Mono.just(dto), CreatorNoteDTO.class)
                );
    }

    public Mono<ServerResponse> addCreatorNote(ServerRequest serverRequest) {
        return
            serverRequest
                .bodyToMono(AddCreatorNoteDTO.class)
                .flatMap(dto -> creatorNoteService.addCreatorNote(dto.getCreatorId(), dto.getText()))
                .map(creatorNoteMapper::toCreatorNoteDTO)
                .flatMap(dto ->
                    ServerResponse
                        .status(HttpStatus.CREATED)
                        .body(Mono.just(dto), CreatorNoteDTO.class)
                );
    }

    public Mono<ServerResponse> editCreatorNote(ServerRequest serverRequest) {
        return
            serverRequest
                .bodyToMono(EditCreatorNoteDTO.class)
                .flatMap(dto -> creatorNoteService.editCreatorNote(Integer.parseInt(serverRequest.pathVariable("id")), dto.getText()))
                .map(creatorNoteMapper::toCreatorNoteDTO)
                .flatMap(dto ->
                    ServerResponse
                        .ok()
                        .body(Mono.just(dto), CreatorNoteDTO.class)
                );
    }

    public Mono<ServerResponse> deleteCreatorNote(ServerRequest serverRequest) {
        return
            Mono
                .fromCallable(() -> serverRequest.pathVariable("id"))
                .map(Integer::parseInt)
                .doOnNext(id -> creatorNoteService.deleteCreatorNote(id))
                .then(
                    ServerResponse
                        .ok()
                        .body(Mono.empty(), String.class)
                );
    }

    private FindCreatorNoteQuery buildQuery(ServerRequest serverRequest) {
        String creatorId = serverRequest.queryParam("creatorId").orElse(null);
        String text = serverRequest.queryParam("text").orElse(null);
        FindCreatorNoteQuery query;

        if (creatorId != null) {
            query = new FindCreatorNoteQuery(Integer.parseInt(creatorId));
        }
        else if (text != null) {
            query = new FindCreatorNoteQuery(text);
        }
        else {
            query = FindCreatorNoteQuery.EMPTY;
        }

        return query;
    }
}
