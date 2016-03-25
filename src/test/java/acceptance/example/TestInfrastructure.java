package acceptance.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class TestInfrastructure {

    private WireMock wireMock;

    public WireMock wireMock() {
        return wireMock;
    }

    public void setUp() {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig());
        wireMock = new WireMock(wireMockServer);
    }

    public void tearDown() {
        wireMock.shutdown();
    }
}
