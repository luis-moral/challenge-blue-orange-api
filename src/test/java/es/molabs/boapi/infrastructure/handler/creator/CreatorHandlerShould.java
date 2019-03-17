package es.molabs.boapi.infrastructure.handler.creator;

import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.domain.creator.Creator;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreatorHandlerShould {

    private static final Map<String, String> NO_FILTERS = null;
    private static final List<String> NO_SORTING = null;

    @Value("${endpoint.creator.path}")
    private String creatorsPath;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreatorService creatorService;
    @MockBean
    private CreatorMapper creatorMapper;
    @MockBean
    private FindCreatorQueryMapper queryMapper;

    @Before
    public void setUp() {
        Mockito
            .when(queryMapper.from(Mockito.any()))
            .thenReturn(new FindCreatorQuery("1", "Some Name", "", "5", "6", "", null));

        Mockito
            .when(creatorService.findCreators(Mockito.any()))
            .thenReturn(Flux.just(new Creator(1, "Some Name", System.currentTimeMillis(), 5, 6)));

        Mockito
            .when(creatorMapper.toCreatorDTO(Mockito.any()))
            .thenReturn(new CreatorDTO(1, "Some Name", System.currentTimeMillis(), 5, 6, ""));
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators() {
        getCreators(NO_FILTERS, NO_SORTING);

        verifyCalled();
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_filtered_by_any_field() {
        Map<String, String> filters = new HashMap<>();
        filters.put("id", "1");

        getCreators(filters, NO_SORTING);

        verifyCalled();
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_sorted_by_any_field() {
        List<String> sorting = new LinkedList<>();
        sorting.add("series");

        getCreators(NO_FILTERS, sorting);

        verifyCalled();
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_filtered_and_sorted_by_any_field() {
        Map<String, String> filters = new HashMap<>();
        filters.put("comics", "2");

        List<String> sorting = new LinkedList<>();
        sorting.add("fullName");

        getCreators(filters, sorting);

        verifyCalled();
    }

    private void verifyCalled() {
        Mockito
            .verify(creatorMapper, Mockito.times(1))
            .toCreatorDTO(Mockito.any());

        Mockito
            .verify(queryMapper, Mockito.times(1))
            .from(Mockito.any());

        Mockito
            .verify(creatorService, Mockito.times(1))
            .findCreators(Mockito.any());
    }

    private void getCreators(Map<String, String> filters, List<String> sorting) {
        webTestClient
            .get()
                .uri(builder -> builder.path(creatorsPath).queryParams(toQueryParams(builder, filters, sorting)).build())
            .exchange()
                .expectStatus()
                    .isOk();
    }

    private MultiValueMap toQueryParams(UriBuilder uriBuilder, Map<String, String> filters, List<String> sorting) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        if (filters != null) {
            queryParams.setAll(filters);
        }

        if (sorting != null && !sorting.isEmpty()) {
            queryParams.put("sorted", sorting);
        }

        return queryParams;
    }
}
