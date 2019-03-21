package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.creator.CreatorHandler;
import es.molabs.boapi.infrastructure.handler.creatornote.CreatorNoteHandler;
import es.molabs.boapi.infrastructure.handler.health.HealthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Value("${endpoint.creators.path}")
    private String creatorsPath;

    @Value("${endpoint.creators-notes.path}")
    private String creatorsNotesPath;

    @Value("${endpoint.creator-note.path}")
    private String creatorNotePath;

    @Value("${endpoint.health.path}")
    private String healthPath;

    @Bean
    public RouterFunction<ServerResponse> routes(
        CreatorHandler creatorHandler,
        CreatorNoteHandler creatorNoteHandler,
        HealthHandler healthHandler
    ) {
        return
            RouterFunctions
                .route(
                    RequestPredicates.GET(creatorsPath),
                    creatorHandler::getCreators
                )
                .andRoute(
                    RequestPredicates.GET(creatorsNotesPath),
                    creatorNoteHandler::getCreatorNotesFiltered
                )
                .andRoute(
                    RequestPredicates.GET(creatorNotePath),
                    creatorNoteHandler::getCreatorNote
                )
                .andRoute(
                    RequestPredicates.POST(creatorNotePath),
                    creatorNoteHandler::addCreatorNote
                )
                .andRoute(
                    RequestPredicates.PUT(creatorNotePath),
                    creatorNoteHandler::editCreatorNote
                )
                .andRoute(
                    RequestPredicates.DELETE(creatorNotePath),
                    creatorNoteHandler::deleteCreatorNote
                )
                .andRoute(
                    RequestPredicates.GET(healthPath),
                    healthHandler::health
                );
    }
}
