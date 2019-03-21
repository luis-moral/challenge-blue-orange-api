package es.molabs.boapi.infrastructure.handler.creatornote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.molabs.boapi.application.CreatorNoteService;
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
    private final ObjectMapper objectMapper;

    public CreatorNoteHandler(CreatorNoteService creatorNoteService, CreatorNoteMapper creatorNoteMapper, ObjectMapper objectMapper) {
        this.creatorNoteService = creatorNoteService;
        this.creatorNoteMapper = creatorNoteMapper;
        this.objectMapper = objectMapper;
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

    private<T> T readValue(String body, Class<T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (IOException IOe) {
            throw new RuntimeException(IOe);
        }
    }
}
