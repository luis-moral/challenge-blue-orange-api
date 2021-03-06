package es.molabs.boapi.infrastructure.repository.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import es.molabs.boapi.domain.creator.SortQuery;
import es.molabs.boapi.infrastructure.handler.creator.FindCreatorQueryMapper;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class MarvelApiClientShould {

    private static final int MARVEL_API_PORT;
    private static final String MARVEL_PUBLIC_API_KEY = "123456789asdfghjkl";
    private static final String MARVEL_PRIVATE_API_KEY = "qwertyuiop12345";

    private static final MarvelCreatorDTO NONE = creatorDTO(7968, "", "-0001-11-30T00:00:00-0500", 0, 0);
    private static final MarvelCreatorDTO ARK = creatorDTO(6606, "A.R.K.", "2007-01-02T00:00:00-0500", 1, 1);
    private static final MarvelCreatorDTO TIM_BRADSTREET = creatorDTO(1, "Tim Bradstreet" , "2010-12-09T11:41:29-0500", 100, 37);

    static {
        MARVEL_API_PORT = 20000 + new Random().nextInt(25000);
    }

    private StaticTimestampGenerator staticTimestampGenerator;
    private WireMockServer marvelApiMock;
    private MarvelApiClient marvelApiClient;

    @Before
    public void setUp() {
        staticTimestampGenerator = new StaticTimestampGenerator();

        marvelApiMock = new WireMockServer(MARVEL_API_PORT);
        marvelApiMock.start();

        marvelApiClient =
            new MarvelApiClient(
                "http://localhost:" + MARVEL_API_PORT,
                MARVEL_PUBLIC_API_KEY,
                MARVEL_PRIVATE_API_KEY,
                WebClient.create(),
                new FindCreatorQueryMapper(),
                new ObjectMapper(),
                staticTimestampGenerator
            );
    }

    @After
    public void tearDown() {
        if (marvelApiMock.isRunning()) {
            marvelApiMock.stop();
        }
    }

    @Test public void
    get_the_creators_without_filters_or_sorting() throws IOException {
        FindCreatorQuery query = FindCreatorQuery.EMPTY;
        String timestamp = getTimestamp();

        stubMarvelFilterCreatorsApi(query, timestamp);

        StepVerifier
            .create(marvelApiClient.get(query))
            .expectNext(NONE, ARK)
            .verifyComplete();

        verifyMarvelFilterCreatorsApi(timestamp);
    }

    @Test public void
    get_the_creators_with_filters_and_without_sorting() throws IOException {
        FindCreatorQuery query = buildQuery();
        String timestamp = getTimestamp();

        stubMarvelFilterCreatorsApi(query, timestamp);

        StepVerifier
                .create(marvelApiClient.get(query))
                .expectNext(NONE, ARK)
                .verifyComplete();

        verifyMarvelFilterCreatorsApi(timestamp);
    }

    @Test public void
    get_the_creators_without_filters_and_with_sorting() throws IOException {
        FindCreatorQuery query =
            new FindCreatorQuery(
                new SortQuery(
                    Arrays
                        .asList(
                            new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_COMICS, SortQuery.SortType.Ascending),
                            new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_SERIES, SortQuery.SortType.Descending),
                            new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_NOTES, SortQuery.SortType.Ascending)
                        )
                )
            );
        String timestamp = getTimestamp();

        stubMarvelFilterCreatorsApi(query, timestamp);

        StepVerifier
            .create(marvelApiClient.get(query))
            .expectNext(NONE, ARK)
            .verifyComplete();

        verifyMarvelFilterCreatorsApi(timestamp);
    }

    @Test public void
    get_the_creators_with_filters_and_sorting() throws IOException {
        FindCreatorQuery query =
            buildQuery(
                new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_COMICS, SortQuery.SortType.Ascending),
                new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_SERIES, SortQuery.SortType.Descending),
                new SortQuery.SortQueryField(FindCreatorQueryMapper.FIELD_NOTES, SortQuery.SortType.Ascending)
            );
        String timestamp = getTimestamp();

        stubMarvelFilterCreatorsApi(query, timestamp);

        StepVerifier
            .create(marvelApiClient.get(query))
            .expectNext(NONE, ARK)
            .verifyComplete();

        verifyMarvelFilterCreatorsApi(timestamp);
    }

    @Test public void
    get_a_creator_by_id() throws IOException {
        int id = 1;
        String timestamp = getTimestamp();

        stubMarvelGetCreatorApi(id, timestamp);

        StepVerifier
            .create(marvelApiClient.get(id))
            .expectNext(TIM_BRADSTREET)
            .verifyComplete();

        verifyMarvelGetCreatorApi(id, timestamp);
    }

    private String getTimestamp() {
        return Long.toString(staticTimestampGenerator.currentId());
    }

    private FindCreatorQuery buildQuery() {
        return buildQuery(new SortQuery.SortQueryField[] {});
    }

    private FindCreatorQuery buildQuery(SortQuery.SortQueryField...fields) {
        String id = "1";
        String fullName = "Some Name";
        String modified = "12445458745";
        String comics = "5";
        String series = "3";
        String notes = "Some Notes";

        return
            new FindCreatorQuery(
                id,
                fullName,
                modified,
                comics,
                series,
                notes,
                fields != null && fields.length > 0 ? new SortQuery(Arrays.asList(fields)) : null
            );
    }

    private Map<String, StringValuePattern> buildQueryMap(FindCreatorQuery query, String timestamp) {
        Map<String, StringValuePattern> map = new HashMap<>();
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_ID, query.getId());
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_NAME_STARTS_WITH, query.getFullName());
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_MODIFIED, query.getModified());
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_COMICS, query.getComics());
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_SERIES, query.getSeries());
        addFieldToMap(map, FindCreatorQueryMapper.FIELD_NOTES, query.getNotes());
        map.put("apikey", WireMock.equalTo(MARVEL_PUBLIC_API_KEY));
        map.put("hash", WireMock.equalTo(getHash(timestamp)));
        map.put("ts", WireMock.equalTo(timestamp));

        if (query.getSortQuery() != null) {
            String sorting =
                query
                    .getSortQuery()
                    .getFields()
                    .stream()
                    .map(field -> field.getType() == SortQuery.SortType.Ascending ? field.getField() : "-" + field.getField())
                    .collect(Collectors.joining(","));

            map.put(FindCreatorQueryMapper.FIELD_SORT_BY, WireMock.equalTo(sorting));
        }

        return map;
    }

    private void addFieldToMap(Map<String, StringValuePattern> map, String field, String value) {
        if (value != null) {
            map.put(field, WireMock.equalTo(value));
        }
    }

    private void stubMarvelFilterCreatorsApi(FindCreatorQuery query, String timestamp) throws IOException {
        Map<String, StringValuePattern> queryParams = buildQueryMap(query, timestamp);

        marvelApiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/v1/public/creators"))
                    .withQueryParams(queryParams)
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                                .withBody(readFile("/creator/get_two_creators.json"))
                    )
            );
    }

    private void stubMarvelGetCreatorApi(int id, String timestamp) throws IOException {
        marvelApiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlPathEqualTo("/v1/public/creators/" + id))
                    .withQueryParam("apikey", WireMock.equalTo(MARVEL_PUBLIC_API_KEY))
                    .withQueryParam("hash", WireMock.equalTo(getHash(timestamp)))
                    .withQueryParam("ts", WireMock.equalTo(timestamp))
                    .willReturn(
                        WireMock
                            .aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                                .withBody(readFile("/creator/get_single_creator_by_id.json"))
                    )
            );
    }

    private void verifyMarvelFilterCreatorsApi(String timestamp) {
        marvelApiMock
            .verify(
                WireMock
                    .getRequestedFor(WireMock.urlPathEqualTo("/v1/public/creators"))
                    .withQueryParam("apikey", WireMock.equalTo(MARVEL_PUBLIC_API_KEY))
                    .withQueryParam("hash", WireMock.equalTo(getHash(timestamp)))
                    .withQueryParam("ts", WireMock.equalTo(timestamp))
            );
    }

    private void verifyMarvelGetCreatorApi(int id, String timestamp) {
        marvelApiMock
            .verify(
                WireMock
                    .getRequestedFor(WireMock.urlPathEqualTo("/v1/public/creators/" + id))
                    .withQueryParam("apikey", WireMock.equalTo(MARVEL_PUBLIC_API_KEY))
                    .withQueryParam("hash", WireMock.equalTo(getHash(timestamp)))
                    .withQueryParam("ts", WireMock.equalTo(timestamp))
            );
    }

    private String getHash(String timestamp) {
        return DigestUtils.md5DigestAsHex((timestamp + MARVEL_PRIVATE_API_KEY + MARVEL_PUBLIC_API_KEY).getBytes());
    }

    private static MarvelCreatorDTO creatorDTO(int id, String fullName, String modified, int comics, int series) {
        return new MarvelCreatorDTO(id, fullName, modified, new MarvelCreatorDTO.ItemsDTO(comics), new MarvelCreatorDTO.ItemsDTO(series));
    }

    private String readFile(String resource) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8);
    }
}