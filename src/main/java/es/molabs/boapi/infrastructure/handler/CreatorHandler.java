package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.application.FindCreatorsQuery;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class CreatorHandler {

    private final CreatorService creatorService;
    private final CreatorMapper mapper;

    public CreatorHandler(CreatorService creatorService) {
        this.creatorService = creatorService;

        mapper = new CreatorMapper();
    }

    public Mono<ServerResponse> getCreators(ServerRequest request) {
        return
            ServerResponse
                .ok()
                .body(
                    Mono
                        .fromCallable(() -> FindCreatorsQuery.from(request.queryParams()))
                        .flatMapIterable(query -> creatorService.findCreators(query))
                        .map(creator -> mapper.toCreatorDTO(creator)),
                    CreatorDTO.class
                );
    }
}
