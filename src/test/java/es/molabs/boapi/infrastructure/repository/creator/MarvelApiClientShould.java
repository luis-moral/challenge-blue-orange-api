package es.molabs.boapi.infrastructure.repository.creator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

@RunWith(JUnit4.class)
public class MarvelApiClientShould {

    private static final int MARVEL_API_PORT;

    static {
        MARVEL_API_PORT = 20000 + new Random().nextInt(25000);
    }

    public WireMockServer marvelApiMock;

    @Before
    public void setUp() {
        marvelApiMock = new WireMockServer(MARVEL_API_PORT);
        marvelApiMock.start();
    }

    @After
    public void tearDown() {
        marvelApiMock.stop();
    }

    @Test public void
    get_the_creators_matching_filter_and_sorting() {
        marvelApiMock
            .stubFor(
                WireMock
                    .get(WireMock.urlEqualTo("/v1/public/creators"))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(200)
                )
            );
    }
}