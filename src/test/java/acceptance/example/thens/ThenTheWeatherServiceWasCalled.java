package acceptance.example.thens;

import acceptance.example.test.TestResult;
import io.github.theangrydev.yatspecfluent.ThenVerification;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ThenTheWeatherServiceWasCalled implements ThenVerification<TestResult> {

    private String city;

    public ThenTheWeatherServiceWasCalled withCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public void verify(TestResult testResult) {
        testResult.verifyThat(getRequestedFor(urlPathEqualTo("/data/2.5/weather"))
                .withQueryParam("q", equalTo(city)));
    }
}
