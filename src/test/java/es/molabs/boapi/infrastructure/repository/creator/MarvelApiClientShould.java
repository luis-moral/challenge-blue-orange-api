package es.molabs.boapi.infrastructure.repository.creator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import es.molabs.boapi.domain.creator.FindCreatorQuery;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@RunWith(JUnit4.class)
public class MarvelApiClientShould {

    private static final int MARVEL_API_PORT;
    private static final String MARVEL_API_KEY = "123456789asdfghjkl";

    static {
        MARVEL_API_PORT = 20000 + new Random().nextInt(25000);
    }

    private WireMockServer marvelApiMock;
    private MarvelApiClient marvelApiClient;

    @Before
    public void setUp() {
        marvelApiMock = new WireMockServer(MARVEL_API_PORT);
        marvelApiMock.start();

        marvelApiClient = new MarvelApiClient("localhost", MARVEL_API_PORT, MARVEL_API_KEY);
    }

    @After
    public void tearDown() {
        marvelApiMock.stop();
    }

    @Test public void
    get_the_creators_matching_filter_and_sorting() throws IOException {
        marvelApiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlEqualTo("/v1/public/creators"))
                    .withQueryParam("apikey", WireMock.equalTo(MARVEL_API_KEY))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withBody(readFile("/creator/get_creators.json"))
                )
            );

        FindCreatorQuery query = FindCreatorQuery.EMPTY;

        marvelApiClient.get(query);
    }

    private String readFile(String resource) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8);
    }
}