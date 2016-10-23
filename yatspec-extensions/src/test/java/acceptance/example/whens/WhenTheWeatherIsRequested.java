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
package acceptance.example.whens;

import acceptance.example.test.TestInfrastructure;
import acceptance.example.test.TestResult;
import io.github.theangrydev.fluentbdd.core.When;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;

public class WhenTheWeatherIsRequested implements When<TestResult>, WithAssertions {

    private final TestInfrastructure testInfrastructure;
    private final String caller;

    private String city;

    public WhenTheWeatherIsRequested(TestInfrastructure testInfrastructure, String caller) {
        this.testInfrastructure = testInfrastructure;
        this.caller = caller;
    }

    @Override
    public TestResult execute() {
        Request request = weatherRequest(testInfrastructure.serverBaseUrl());
        testInfrastructure.recordIncomingRequest(caller, request);

        Response response = testInfrastructure.execute(request);
        testInfrastructure.recordOutgoingResponse(caller, response);
        return new TestResult(testInfrastructure, response);
    }

    private Request weatherRequest(String baseUrl) {
        HttpUrl weatherUrl = HttpUrl.parse(baseUrl).newBuilder().addPathSegment("weather").addQueryParameter("city", this.city).build();
        return new Request.Builder().url(weatherUrl).build();
    }

    public WhenTheWeatherIsRequested forCity(String city) {
        this.city = city;
        return this;
    }

    public WhenTheWeatherIsRequested requestsTheWeather() {
        return this;
    }
}
