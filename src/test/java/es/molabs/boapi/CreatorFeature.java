package es.molabs.boapi;

import es.molabs.boapi.domain.Creator;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
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
@SpringBootTest(
	classes = Application.class,
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CreatorFeature {

	private static final Map<String, String> NO_FILTERS = null;
	private static final List<String> NO_SORTING = null;

	private static final Creator MAGGIE = new Creator(1, "Maggie One", System.currentTimeMillis(), 2, 4);
	private static final Creator CHARLIE = new Creator(2, "Charlie Two", System.currentTimeMillis(), 4, 4);
	private static final Creator JOHN = new Creator(3, "John Three", System.currentTimeMillis(), 1, 8);

	@Value("${endpoint.creator.path}")
	private String creatorsPath;

	@Autowired
	private WebTestClient webTestClient;

	@Test public void
	clients_can_get_a_list_of_creators() {
		assertCreators(NO_FILTERS, NO_SORTING, MAGGIE, CHARLIE, JOHN);
	}

	@Test public void
	clients_can_get_a_list_of_creators_filtered_by_any_field() {
		Map<String, String> filters = new HashMap<>();
		filters.put("id", "1");

		assertCreators(filters, NO_SORTING, MAGGIE);
	}

	@Test public void
	clients_can_get_a_list_of_creators_sorted_by_any_field() {
		List<String> sorting = new LinkedList<>();
		sorting.add("series");

		assertCreators(NO_FILTERS, sorting, MAGGIE);
	}

	@Test public void
	clients_can_get_a_list_of_creators_filtered_and_sorted_by_any_field() {
		Map<String, String> filters = new HashMap<>();
		filters.put("comics", "2");

		List<String> sorting = new LinkedList<>();
		sorting.add("fullName");

		assertCreators(filters, sorting, CHARLIE, MAGGIE);
	}

	private void assertCreators(Map<String, String> filters, List<String> sorting, Creator...expectedCreators) {
		webTestClient
			.get()
				.uri(builder -> builder.path(creatorsPath).queryParams(toQueryParams(builder, filters, sorting)).build())
			.exchange()
				.expectStatus()
					.isOk()
				.expectBody(new ParameterizedTypeReference<List<Creator>>() {})
					.consumeWith(response ->
						Assertions
							.assertThat(response.getResponseBody())
							.containsSequence(expectedCreators)
					);
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
