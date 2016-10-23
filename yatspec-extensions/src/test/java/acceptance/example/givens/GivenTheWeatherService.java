/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of fluent-bdd.
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
import io.github.theangrydev.fluentbdd.core.Given;
import io.github.theangrydev.fluentbdd.yatspec.WriteOnlyTestItems;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GivenTheWeatherService implements Given {

    private final WriteOnlyTestItems writeOnlyTestItems;
    private final TestInfrastructure testInfrastructure;

    private String description;
    private String cityName;

    public GivenTheWeatherService(WriteOnlyTestItems writeOnlyTestItems, TestInfrastructure testInfrastructure) {
        this.writeOnlyTestItems = writeOnlyTestItems;
        this.testInfrastructure = testInfrastructure;
    }

    public GivenTheWeatherService willReturn() {
        return this;
    }

    public GivenTheWeatherService weatherDescription(String description) {
        this.description = description;
        return this;
    }

    public GivenTheWeatherService forCity(String cityName) {
        this.cityName = cityName;
        return this;
    }

    @Override
    public void prime() {
        writeOnlyTestItems.addToGivens("City", cityName);
        testInfrastructure.givenThat("OpenWeatherMap", get(urlPathMatching("/data/2.5/weather"))
                .withQueryParam("q", equalTo(cityName))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(weatherWithDescription())));
    }

    private String weatherWithDescription() {
        return "{\"weather\":[{\"description\":\"" + description + "\"}]}";
    }
}
