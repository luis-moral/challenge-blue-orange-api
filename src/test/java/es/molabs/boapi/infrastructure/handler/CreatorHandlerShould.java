package es.molabs.boapi.infrastructure.handler;

import es.molabs.boapi.application.CreatorService;
import es.molabs.boapi.application.FindCreatorsQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilder;

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
    private CreatorService creatorService;

    @Test public void
    allow_clients_to_get_a_list_of_creators() {
        getCreators(NO_FILTERS, NO_SORTING);

        Mockito
            .verify(creatorService, Mockito.times(1))
            .findCreators(query(null, null));
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_filtered_by_any_field() {
        Map<String, String> filters = new HashMap<>();
        filters.put("id", "1");

        getCreators(filters, NO_SORTING);

        Mockito
            .verify(creatorService, Mockito.times(1))
            .findCreators(query(filters, null));
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_sorted_by_any_field() {
        List<String> sorting = new LinkedList<>();
        sorting.add("series");

        getCreators(NO_FILTERS, sorting);

        Mockito
            .verify(creatorService, Mockito.times(1))
            .findCreators(query(null, sorting));
    }

    @Test public void
    allow_clients_to_get_a_list_of_creators_filtered_and_sorted_by_any_field() {
        Map<String, String> filters = new HashMap<>();
        filters.put("comics", "2");

        List<String> sorting = new LinkedList<>();
        sorting.add("fullName");

        getCreators(filters, sorting);

        Mockito
            .verify(creatorService, Mockito.times(1))
            .findCreators(query(filters, sorting));
    }

    private FindCreatorsQuery query(Map<String, String> filters, List<String> sorting) {
        return new FindCreatorsQuery();
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
