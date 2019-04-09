package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.application.CreatorService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreatorHandler {

    private final CreatorService creatorService;
    private final CreatorMapper creatorMapper;
    private final FindCreatorQueryMapper queryMapper;

    public CreatorHandler(CreatorService creatorService, CreatorMapper creatorMapper, FindCreatorQueryMapper queryMapper) {
        this.creatorService = creatorService;
        this.creatorMapper = creatorMapper;
        this.queryMapper = queryMapper;
    }

    public Mono<ServerResponse> getCreatorsByQuery(ServerRequest request) {
        return
            creatorService
                .find(queryMapper.fromQuery(request.queryParams()))
                .map(creator -> creatorMapper.toCreatorDTO(creator))
                .collectList()
                .flatMap(dtoList ->
                    ServerResponse
                        .ok()
                        .body(Flux.fromIterable(dtoList), CreatorDTO.class)
                );
    }

    public Mono<ServerResponse> getCreator(ServerRequest request) {
        return
            creatorService
                .findById(Integer.parseInt(request.pathVariable("id")))
                .map(creator -> creatorMapper.toCreatorDTO(creator))
                .flatMap(dto ->
                    ServerResponse
                        .ok()
                        .body(Mono.just(dto), CreatorDTO.class)
                );
    }
}
