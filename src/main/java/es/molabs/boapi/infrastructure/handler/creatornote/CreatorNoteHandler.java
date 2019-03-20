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
    private final ObjectMapper objectMapper;

    public CreatorNoteHandler(CreatorNoteService creatorNoteService, ObjectMapper objectMapper) {
        this.creatorNoteService = creatorNoteService;
        this.objectMapper = objectMapper;
    }

    public Mono<ServerResponse> addCreatorNote(ServerRequest serverRequest) {
        return
            serverRequest
                .bodyToMono(String.class)
                .map(body -> {
                    try {
                        return objectMapper.readValue(body, AddCreatorNoteDTO.class);
                    } catch (IOException IOe) {
                        throw new RuntimeException(IOe);
                    }
                })
                .doOnNext(dto -> creatorNoteService.addCreatorNote(dto))
                .flatMap(dto ->
                    ServerResponse
                        .status(HttpStatus.CREATED)
                        .body(Mono.just(dto), AddCreatorNoteDTO.class)
                );
    }

    public Mono<ServerResponse> editCreatorNote(ServerRequest serverRequest) {
        throw new UnsupportedOperationException();
    }

    public Mono<ServerResponse> deleteCreatorNote(ServerRequest serverRequest) {
        throw new UnsupportedOperationException();
    }
}
