package es.molabs.boapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.boapi.domain.creatornote.CreatorNoteRepository;
import es.molabs.boapi.infrastructure.handler.creator.CreatorDTO;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(
	classes = Application.class,
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class CreatorFeature {

	private static final Map<String, String> NO_FILTERS = null;
	private static final List<String> NO_SORTING = null;

	private static final CreatorDTO NONE = new CreatorDTO(7968, "", "-0001-11-30T00:00:00-0500", 0, 0, null);
	private static final CreatorDTO ARK = new CreatorDTO(6606, "A.R.K.", "2007-01-02T00:00:00-0500", 1, 1, null);
	private static final CreatorDTO TIM_BRADSTREET = new CreatorDTO(1, "Tim Bradstreet" , "2010-12-09T11:41:29-0500", 100, 37, null);

	@Value("${endpoint.creators.path}")
	private String creatorsPath;
	@Value("${endpoint.creator.path}")
	private String creatorPath;
	@Value("${marvel.api.base-url}")
	private String marvelBaseUrl;
	@Value("${marvel.api.key}")
	private String marvelApiKey;
	@Value("${redis.port}")
	private int redisPort;

	@Autowired
	private WebTestClient webTestClient;
	@Autowired
	private CreatorNoteRepository creatorNoteRepository;

	private WireMockServer marvelApiMock;
	private RedisServer redisServer;
	
	@Before
	public void setUp() throws URISyntaxException {
		URI marvelBaseUri = new URI(marvelBaseUrl);
		int port =
			marvelBaseUri.getPort() != -1
				? marvelBaseUri.getPort()
				: marvelBaseUri.getScheme().startsWith("https") ? 443 : 80;

		marvelApiMock = new WireMockServer(port);
		marvelApiMock.start();

		redisServer = new RedisServer(redisPort);
		redisServer.start();
	}

	@After
	public void tearDown() {
		if (marvelApiMock.isRunning()) {
			marvelApiMock.stop();
		}

		if (redisServer.isActive()) {
			redisServer.stop();
		}
	}

	@Test public void
	clients_can_get_a_creator_by_id() throws IOException {
		CreatorDTO creatorWithCustomNote =
			new CreatorDTO(
				TIM_BRADSTREET.getId(),
				TIM_BRADSTREET.getFullName(),
				TIM_BRADSTREET.getModified(),
				TIM_BRADSTREET.getComics(),
				TIM_BRADSTREET.getSeries(),
				"Some custom note"
			);

		stubMarvelApi(creatorWithCustomNote.getId());

		creatorNoteRepository
			.add(creatorWithCustomNote.getId(), creatorWithCustomNote.getNote())
			.block();

		assertCreator(creatorWithCustomNote);
	}

	@Test public void
	clients_can_get_a_list_of_creators() throws IOException {
		stubMarvelApi();

		CreatorDTO creatorWithCustomNote =
			new CreatorDTO(
				ARK.getId(),
				ARK.getFullName(),
				ARK.getModified(),
				ARK.getComics(),
				ARK.getSeries(),
				"Some custom note"
			);

		creatorNoteRepository
			.add(creatorWithCustomNote.getId(), creatorWithCustomNote.getNote())
			.block();

		assertCreators(NO_FILTERS, NO_SORTING, NONE, creatorWithCustomNote);
	}

	@Test public void
	clients_can_get_a_list_of_creators_filtered_by_any_field() throws IOException {
		stubMarvelApi();

		Map<String, String> filters = new HashMap<>();
		filters.put("id", "1");

		assertCreators(filters, NO_SORTING, NONE, ARK);
	}

	@Test public void
	clients_can_get_a_list_of_creators_sorted_by_any_field() throws IOException {
		stubMarvelApi();

		List<String> sorting = new LinkedList<>();
		sorting.add("series");

		assertCreators(NO_FILTERS, sorting, NONE, ARK);
	}

	@Test public void
	clients_can_get_a_list_of_creators_filtered_and_sorted_by_any_field() throws IOException {
		stubMarvelApi();

		Map<String, String> filters = new HashMap<>();
		filters.put("comics", "2");

		List<String> sorting = new LinkedList<>();
		sorting.add("fullName");

		assertCreators(filters, sorting, NONE, ARK);
	}

	private void stubMarvelApi(int id) throws IOException {
		marvelApiMock
			.stubFor(
				WireMock
					.get(WireMock.urlPathEqualTo("/v1/public/creators/" + id))
					.withQueryParam("apikey", WireMock.equalTo(marvelApiKey))
					.willReturn(
						WireMock
							.aResponse()
								.withStatus(200)
								.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
								.withBody(readFile("/creator/get_single_creator_by_id.json"))
					)
			);
	}

	private void stubMarvelApi() throws IOException {
		marvelApiMock
			.stubFor(
				WireMock
					.get(WireMock.urlPathEqualTo("/v1/public/creators"))
					.withQueryParam("apikey", WireMock.equalTo(marvelApiKey))
					.willReturn(
						WireMock
							.aResponse()
								.withStatus(200)
								.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
								.withBody(readFile("/creator/get_two_creators.json"))
					)
			);
	}

	private void assertCreator(CreatorDTO expectedCreator) {
		webTestClient
			.get()
				.uri(builder -> builder.path(creatorPath).build(expectedCreator.getId()))
			.exchange()
				.expectStatus()
					.isOk()
				.expectBody(CreatorDTO.class)
					.consumeWith(response ->
						Assertions
							.assertThat(response.getResponseBody())
							.isEqualTo(expectedCreator)
				);
	}

	private void assertCreators(Map<String, String> filters, List<String> sorting, CreatorDTO...expectedCreators) {
		webTestClient
			.get()
				.uri(builder -> builder.path(creatorsPath).queryParams(toQueryParams(filters, sorting)).build())
			.exchange()
				.expectStatus()
					.isOk()
				.expectBody(new ParameterizedTypeReference<List<CreatorDTO>>() {})
					.consumeWith(response ->
						Assertions
							.assertThat(response.getResponseBody())
							.containsSequence(expectedCreators)
					);
	}

	private MultiValueMap toQueryParams(Map<String, String> filters, List<String> sorting) {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

		if (filters != null) {
			queryParams.setAll(filters);
		}

		if (sorting != null && !sorting.isEmpty()) {
			queryParams.put("sorted", sorting);
		}

		return queryParams;
	}

	private String readFile(String resource) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8);
	}
}
