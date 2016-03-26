package acceptance.example.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.theangrydev.yatspecfluent.InterestingTestItems;
import io.github.theangrydev.yatspecfluent.Primer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GivenOpenWeatherMap implements Primer<TestInfrastructure> {
    private String description;
    private String cityName;

    public GivenOpenWeatherMap willReturn() {
        return this;
    }

    public GivenOpenWeatherMap weatherDescription(String description) {
        this.description = description;
        return this;
    }

    public GivenOpenWeatherMap forCity(String cityName) {
        this.cityName = cityName;
        return this;
    }

    @Override
    public void prime(InterestingTestItems interestingTestItems, TestInfrastructure testInfrastructure) {
        interestingTestItems.addToGivens("City", cityName);
        WireMock wireMock = testInfrastructure.wireMock();
        wireMock.register(get(urlPathMatching("/data/2.5/weather")).withQueryParam("q", equalTo(cityName)).willReturn(aResponse().withStatus(200).withBody(weatherWithDescription())));
    }

    private String weatherWithDescription() {
        return "{\"weather\":[{\"description\":\"" + description + "\"}]}";
    }
}
