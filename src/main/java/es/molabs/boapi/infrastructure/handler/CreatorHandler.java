package es.molabs.boapi.infrastructure.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class CreatorHandler {
    public Mono<ServerResponse> getCreators(ServerRequest request) {
        throw new UnsupportedOperationException();
    }
}
