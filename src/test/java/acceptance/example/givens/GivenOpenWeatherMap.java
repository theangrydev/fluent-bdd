/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of yatspec-fluent.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package acceptance.example.givens;

import acceptance.example.test.TestInfrastructure;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.theangrydev.yatspecfluent.Given;
import io.github.theangrydev.yatspecfluent.ReadOnlyTestItems;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GivenOpenWeatherMap implements Given {

    private final ReadOnlyTestItems readOnlyTestItems;
    private final TestInfrastructure testInfrastructure;

    private String description;
    private String cityName;

    public GivenOpenWeatherMap(ReadOnlyTestItems readOnlyTestItems, TestInfrastructure testInfrastructure) {
        this.readOnlyTestItems = readOnlyTestItems;
        this.testInfrastructure = testInfrastructure;
    }

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
    public void prime() {
        readOnlyTestItems.addToGivens("City", cityName);
        WireMock wireMock = testInfrastructure.wireMock();
        wireMock.register(get(urlPathMatching("/data/2.5/weather")).withQueryParam("q", equalTo(cityName)).willReturn(aResponse().withStatus(200).withBody(weatherWithDescription())));
    }

    private String weatherWithDescription() {
        return "{\"weather\":[{\"description\":\"" + description + "\"}]}";
    }
}
