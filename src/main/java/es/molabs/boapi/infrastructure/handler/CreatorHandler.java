package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.application.CreatorService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    public Mono<ServerResponse> getCreators(ServerRequest request) {
        return
            ServerResponse
                .ok()
                .body(
                    creatorService
                        .findCreators(queryMapper.from(request.queryParams()))
                        .map(creator -> creatorMapper.toCreatorDTO(creator)),
                    CreatorDTO.class
                );
    }
}
