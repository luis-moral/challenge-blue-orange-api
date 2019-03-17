package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.infrastructure.handler.creator.CreatorHandler;
import es.molabs.boapi.infrastructure.handler.creator.CreatorMapper;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public CreatorHandler creatorHandler(
        CreatorService creatorService,
        CreatorMapper creatorMapper,
        FindCreatorQueryMapper queryMapper
    ) {
        return new CreatorHandler(creatorService, creatorMapper, queryMapper);
    }
}
