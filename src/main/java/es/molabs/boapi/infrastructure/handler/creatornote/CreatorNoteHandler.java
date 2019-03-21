package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.application.CreatorNoteService;
import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.domain.creatornote.FindCreatorNoteQuery;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatorNoteHandler {

    private final CreatorNoteService creatorNoteService;
    private final CreatorNoteMapper creatorNoteMapper;
    private final CreatorService creatorService;
    private final ObjectMapper objectMapper;

    public CreatorNoteHandler(CreatorNoteService creatorNoteService, CreatorNoteMapper creatorNoteMapper, CreatorService creatorService, ObjectMapper objectMapper) {
        this.creatorNoteService = creatorNoteService;
        this.creatorNoteMapper = creatorNoteMapper;
        this.creatorService = creatorService;
        this.objectMapper = objectMapper;
    }

    public Mono<ServerResponse> getCreatorNotesFiltered(ServerRequest serverRequest) {
        return
            ServerResponse
                .ok()
                .body(
                    creatorNoteService
                        .find(buildQuery(serverRequest))
                        .flatMap(creatorNote ->
                            creatorService
                                .findById(creatorNote.getCreatorId())
                                .map(creator -> creatorNoteMapper.toCreatorNoteDTO(creatorNote, creator.getFullName()))
                        ),
                    CreatorNoteDTO.class
                );
    }

    public Mono<ServerResponse> getCreatorNote(ServerRequest serverRequest) {
        throw new UnsupportedOperationException();
    }

    public Mono<ServerResponse> addCreatorNote(ServerRequest serverRequest) {
        return
            serverRequest
                .bodyToMono(String.class)
                .map(body -> readValue(body, AddCreatorNoteDTO.class))
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
                .bodyToMono(String.class)
                .map(body -> readValue(body, EditCreatorNoteDTO.class))
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
        FindCreatorNoteQuery query = null;

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

    private<T> T readValue(String body, Class<T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (IOException IOe) {
            throw new RuntimeException(IOe);
        }
    }
}
