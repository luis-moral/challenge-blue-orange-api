package es.molabs.boapi.infrastructure.configuration;

import es.molabs.boapi.infrastructure.handler.CreatorMapper;
import es.molabs.boapi.infrastructure.handler.FindCreatorQueryMapper;
import es.molabs.boapi.infrastructure.repository.MarvelCreatorMapper;
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
