package acceptance.example.test;

import acceptance.example.production.WeatherApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.theangrydev.yatspecfluent.ReadOnlyTestItems;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class TestInfrastructure implements ReadOnlyTestItems {

    private WireMock wireMock;
    private WeatherApplication weatherApplication;

    private final ReadOnlyTestItems readOnlyTestItems;

    public TestInfrastructure(ReadOnlyTestItems readOnlyTestItems) {
        this.readOnlyTestItems = readOnlyTestItems;
    }

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

    @Override
    public void addToGivens(String key, Object instance) {
        readOnlyTestItems.addToGivens(key, instance);
    }

    @Override
    public void addToCapturedInputsAndOutputs(String key, Object instance) {
        readOnlyTestItems.addToGivens(key, instance);
    }
}
