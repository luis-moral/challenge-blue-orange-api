package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.creator.CreatorMapper;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import es.molabs.boapi.infrastructure.repository.creator.MarvelCreatorMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public CreatorMapper creatorMapper() {
        return new CreatorMapper();
    }

    @Bean
    public FindCreatorQueryMapper queryMapper() {
        return new FindCreatorQueryMapper();
    }

    @Bean
    public MarvelCreatorMapper marvelCreatorMapper() {
        return new MarvelCreatorMapper();
    }
}
