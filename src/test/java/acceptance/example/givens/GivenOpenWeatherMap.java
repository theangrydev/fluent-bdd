package acceptance.example.givens;

import acceptance.example.test.TestInfrastructure;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.theangrydev.yatspecfluent.ReadOnlyTestItems;
import io.github.theangrydev.yatspecfluent.Given;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GivenOpenWeatherMap implements Given<TestInfrastructure> {
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
    public void prime(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure) {
        readOnlyTestItems.addToGivens("City", cityName);
        WireMock wireMock = testInfrastructure.wireMock();
        wireMock.register(get(urlPathMatching("/data/2.5/weather")).withQueryParam("q", equalTo(cityName)).willReturn(aResponse().withStatus(200).withBody(weatherWithDescription())));
    }

    private String weatherWithDescription() {
        return "{\"weather\":[{\"description\":\"" + description + "\"}]}";
    }
}
