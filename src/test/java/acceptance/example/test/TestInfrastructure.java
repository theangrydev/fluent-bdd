package acceptance.example.test;

import acceptance.example.production.WeatherApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class TestInfrastructure {

    private WireMock wireMock;
    private WeatherApplication weatherApplication;

    public WireMock wireMock() {
        return wireMock;
    }

    public String serverBaseUrl() {
        return weatherApplication.baseUrl();
    }

    public void setUp() {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(1235));
        wireMockServer.start();
        wireMock = new WireMock(wireMockServer);

        String wireMockServerUrl = String.format("http://localhost:%d", wireMockServer.port());
        weatherApplication = new WeatherApplication(1234, wireMockServerUrl);
        weatherApplication.start();
    }

    public void tearDown() {
        wireMock.shutdown();
        weatherApplication.stop();
    }

    public Response execute(Request request) {
        try {
            return new OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
