package es.molabs.boapi.infrastructure.repository.creator;

import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MarvelApiCreatorRepositoryShould {

    @Mock
    private MarvelApiClient apiClient;
    @Mock
    private MarvelCreatorMapper mapper;

    private MarvelApiCreatorRepository creatorRepository;

    @Before
    public void setUp() {
        creatorRepository = new MarvelApiCreatorRepository(apiClient, mapper);
    }

    @Test public void
    retrieve_the_creators() {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        MarvelCreatorDTO firstCreatorDTO = new MarvelCreatorDTO(1, "Name First", 123, 5, 6);
        MarvelCreatorDTO secondCreatorDTO = new MarvelCreatorDTO(2, "Name Second", 126, 3, 2);
        List<MarvelCreatorDTO> apiClientCreators = Arrays.asList(firstCreatorDTO, secondCreatorDTO);

        Creator firstCreator = Mockito.mock(Creator.class);
        Creator secondCreator = Mockito.mock(Creator.class);

        Mockito
            .when(apiClient.get(Mockito.any()))
            .thenReturn(apiClientCreators);

        Mockito
            .when(mapper.toCreator(firstCreatorDTO))
            .thenReturn(firstCreator);

        Mockito
            .when(mapper.toCreator(secondCreatorDTO))
            .thenReturn(secondCreator);

        StepVerifier
            .create(creatorRepository.find(query))
            .expectNext(firstCreator, secondCreator)
            .verifyComplete();

        Mockito
            .verify(apiClient, Mockito.times(1))
            .get(query);

        Mockito
            .verify(mapper, Mockito.times(1))
            .toCreator(firstCreatorDTO);

        Mockito
            .verify(mapper, Mockito.times(1))
            .toCreator(secondCreatorDTO);
    }
}